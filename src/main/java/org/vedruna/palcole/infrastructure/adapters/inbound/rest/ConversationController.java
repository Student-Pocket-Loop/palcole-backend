package org.vedruna.palcole.infrastructure.adapters.inbound.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.conversations.DeleteConversationUseCase;
import org.vedruna.palcole.domain.ports.inbound.conversations.NewConversationUseCase;
import org.vedruna.palcole.domain.ports.inbound.conversations.SelectAllConversationsUseCase;
import org.vedruna.palcole.domain.ports.inbound.conversations.SelectConversationByIdUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.ConversationInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.conversations.ConversationDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.conversations.InsertConversationDTO;

@Tag(
    name = "Conversations",
    description = "Gestión de conversaciones entre estudiantes sobre artículos académicos")
@CrossOrigin
@AllArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/v1/conversations")
public class ConversationController {

  private final NewConversationUseCase newConversationUseCase;
  private final DeleteConversationUseCase deleteConversationUseCase;
  private final SelectAllConversationsUseCase selectAllConversationsUseCase;
  private final SelectConversationByIdUseCase selectConversationByIdUseCase;
  private final ConversationInConverter conversationInConverter;

  @Operation(
      summary = "Listar todas las conversaciones (paginado)",
      description =
          "Devuelve una página con las conversaciones registradas. Requiere rol USER o ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista paginada de conversaciones recuperada correctamente"),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de paginación u ordenación inválidos",
            content = @Content),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado (token JWT ausente o inválido)",
            content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol USER o ADMIN)",
            content = @Content)
      })
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @GetMapping("/")
  public ResponseEntity<Page<ConversationDTO>> getAllConversations(
      @ParameterObject Pageable pageable) {
    PageableModel pageableModel = conversationInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        conversationInConverter.toConversationsPage(
            selectAllConversationsUseCase.selectAllConversations(pageableModel), pageableModel));
  }

  @Operation(
      summary = "Iniciar una nueva conversación",
      description =
          "Crea una nueva conversación entre dos usuarios sobre un artículo académico. Requiere rol USER o ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Conversación creada exitosamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ConversationDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Conversación iniciada (Usuario Normal)",
                          summary = "Respuesta tras abrir chat como comprador (ROLE_USER)",
                          value =
                              """
                              {
                                "conversationId": 1,
                                "itemId": 1,
                                "buyerId": 1,
                                "createdAt": "2026-09-01T11:30:00"
                              }
                              """),
                      @ExampleObject(
                          name = "Conversación iniciada (Administrador)",
                          summary = "Respuesta tras abrir chat como administrador (ROLE_ADMIN)",
                          value =
                              """
                              {
                                "conversationId": 2,
                                "itemId": 1,
                                "buyerId": 2,
                                "createdAt": "2026-09-01T12:00:00"
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos o campos obligatorios ausentes",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol USER o ADMIN)",
            content = @Content)
      })
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @PostMapping("/")
  public ResponseEntity<ConversationDTO> insertIntoConversation(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Identificadores del artículo y del comprador interesado",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = InsertConversationDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Iniciar Conversación",
                            summary = "Ejemplo de creación de chat por el artículo 1",
                            value =
                                """
                                {
                                    "itemId": 1,
                                    "buyerId": 1
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          InsertConversationDTO insertConversationDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            conversationInConverter.toDto(
                newConversationUseCase.newConversation(
                    conversationInConverter.toDomainInsert(insertConversationDTO))));
  }

  @Operation(
      summary = "Obtener una conversación por su ID",
      description =
          "Busca y devuelve los detalles de una conversación específica a partir de su identificador. Requiere rol USER o ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Conversación encontrada",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ConversationDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Detalle de Conversación",
                          summary = "Respuesta con los datos de la conversación 1",
                          value =
                              """
                              {
                                "conversationId": 1,
                                "itemId": 1,
                                "buyerId": 1,
                                "createdAt": "2026-09-01T11:30:00"
                              }
                              """)
                    })),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ninguna conversación con el ID proporcionado",
            content = @Content)
      })
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @GetMapping("/{conversationId}")
  public ResponseEntity<ConversationDTO> getConversationById(
      @Parameter(description = "ID único de la conversación", example = "1") @PathVariable
          Integer conversationId) {
    return ResponseEntity.ok(
        conversationInConverter.toDto(
            selectConversationByIdUseCase.selectConversationById(conversationId)));
  }

  @Operation(
      summary = "Eliminar una conversación",
      description = "Elimina permanentemente una conversación por su ID. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "Conversación eliminada correctamente (sin contenido)",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol ADMIN, denegado para ROLE_USER)",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ninguna conversación con el ID proporcionado",
            content = @Content)
      })
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{conversationId}")
  public ResponseEntity<Void> deleteConversation(
      @Parameter(description = "ID único de la conversación a eliminar", example = "1")
          @PathVariable
          Integer conversationId) {
    deleteConversationUseCase.deleteConversation(conversationId);
    return ResponseEntity.noContent().build();
  }
}
