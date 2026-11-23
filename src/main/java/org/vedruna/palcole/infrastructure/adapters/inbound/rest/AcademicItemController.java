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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.academicItems.DeleteAcademicItemUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.NewAcademicItemUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.SelectAcademicItemByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.SelectAllAcademicItemsUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.UpdateAcademicItemUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.AcademicItemInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems.AcademicItemDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems.InsertAcademicItemDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems.UpdateAcademicItemDTO;

@Tag(
    name = "Academic Items",
    description = "Gestión de artículos académicos (libros, material escolar, apuntes, etc.)")
@CrossOrigin
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/academic-items")
public class AcademicItemController {

  private final NewAcademicItemUseCase newAcademicItemUseCase;
  private final DeleteAcademicItemUseCase deleteAcademicItemUseCase;
  private final SelectAllAcademicItemsUseCase selectAllAcademicItemsUseCase;
  private final SelectAcademicItemByIdUseCase selectAcademicItemByIdUseCase;
  private final UpdateAcademicItemUseCase updateAcademicItemUseCase;
  private final AcademicItemInConverter academicItemInConverter;

  @Operation(
      summary = "Listar todos los artículos académicos (paginado)",
      description =
          "Devuelve una página con los artículos académicos registrados. Permite paginación y ordenación mediante parámetros query.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista paginada de artículos recuperada correctamente"),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de paginación u ordenación inválidos",
            content = @Content)
      })
  @GetMapping("/")
  public ResponseEntity<Page<AcademicItemDTO>> getAllAcademicItems(
      @ParameterObject Pageable pageable) {
    PageableModel pageableModel = academicItemInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        academicItemInConverter.toAcademicItemsPage(
            selectAllAcademicItemsUseCase.selectAllAcademicItems(pageableModel), pageableModel));
  }

  @Operation(
      summary = "Publicar un nuevo artículo académico",
      description =
          "Crea y registra un nuevo artículo académico asociado a un estudiante y categoría. Requiere rol USER o ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Artículo académico creado exitosamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AcademicItemDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Libro creado (Usuario Normal)",
                          summary = "Respuesta tras publicar un libro con ROLE_USER",
                          value =
                              """
                              {
                                "itemId": 1,
                                "studentId": 1,
                                "categoryId": 1,
                                "title": "Libro de Programación",
                                "description": "Apenas usado",
                                "price": 12.50,
                                "itemCondition": "COMO_NUEVO",
                                "listingType": "VENTA",
                                "saleStatus": "DISPONIBLE",
                                "imageUrl": "https://ejemplo.com/libro.jpg"
                              }
                              """),
                      @ExampleObject(
                          name = "Material creado (Administrador)",
                          summary = "Respuesta tras publicar material con ROLE_ADMIN",
                          value =
                              """
                              {
                                "itemId": 2,
                                "studentId": 1,
                                "categoryId": 2,
                                "title": "Calculadora Científica Casio",
                                "description": "Donación del centro educativo",
                                "price": 0.00,
                                "itemCondition": "BUEN_ESTADO",
                                "listingType": "DONACION",
                                "saleStatus": "DISPONIBLE",
                                "imageUrl": "https://ejemplo.com/calculadora.jpg"
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos o campos obligatorios ausentes",
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
  @PostMapping("/")
  public ResponseEntity<AcademicItemDTO> insertIntoAcademicItem(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Datos del nuevo artículo académico",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = InsertAcademicItemDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Libro de Programación (Venta)",
                            summary = "Ejemplo de venta de libro por estudiante",
                            value =
                                """
                                {
                                    "studentId": 1,
                                    "categoryId": 1,
                                    "title": "Libro de Programación",
                                    "description": "Apenas usado",
                                    "price": 12.50,
                                    "itemCondition": "COMO_NUEVO",
                                    "listingType": "VENTA",
                                    "imageUrl": "https://ejemplo.com/libro.jpg"
                                }
                                """),
                        @ExampleObject(
                            name = "Material Escolar (Donación)",
                            summary = "Ejemplo de donación de material",
                            value =
                                """
                                {
                                    "studentId": 1,
                                    "categoryId": 2,
                                    "title": "Calculadora Científica Casio",
                                    "description": "Donación del centro educativo",
                                    "price": 0.00,
                                    "itemCondition": "BUEN_ESTADO",
                                    "listingType": "DONACION",
                                    "imageUrl": "https://ejemplo.com/calculadora.jpg"
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          InsertAcademicItemDTO insertAcademicItemDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            academicItemInConverter.toDto(
                newAcademicItemUseCase.newAcademicItem(
                    academicItemInConverter.toDomainInsert(insertAcademicItemDTO))));
  }

  @Operation(
      summary = "Obtener un artículo académico por su ID",
      description =
          "Busca y devuelve los detalles de un artículo académico específico a partir de su identificador.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Artículo encontrado",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AcademicItemDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Detalle de Artículo",
                          summary = "Ejemplo de respuesta de libro registrado",
                          value =
                              """
                              {
                                "itemId": 1,
                                "studentId": 1,
                                "categoryId": 1,
                                "title": "Libro de Programación",
                                "description": "Apenas usado",
                                "price": 12.50,
                                "itemCondition": "COMO_NUEVO",
                                "listingType": "VENTA",
                                "saleStatus": "DISPONIBLE",
                                "imageUrl": "https://ejemplo.com/libro.jpg"
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ningún artículo con el ID proporcionado",
            content = @Content)
      })
  @GetMapping("/{itemId}")
  public ResponseEntity<AcademicItemDTO> getAcademicItemById(
      @Parameter(description = "ID único del artículo académico", example = "1") @PathVariable
          Integer itemId) {
    return ResponseEntity.ok(
        academicItemInConverter.toDto(
            selectAcademicItemByIdUseCase.selectAcademicItemById(itemId)));
  }

  @Operation(
      summary = "Actualizar un artículo académico",
      description =
          "Modifica los datos de un artículo académico existente. Requiere rol USER o ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Artículo actualizado correctamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AcademicItemDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Libro actualizado (Reservado)",
                          summary = "Respuesta tras actualizar precio y estado a RESERVADO",
                          value =
                              """
                              {
                                "itemId": 1,
                                "studentId": 1,
                                "categoryId": 1,
                                "title": "Libro de Programación",
                                "description": "Apenas usado",
                                "price": 10.00,
                                "itemCondition": "COMO_NUEVO",
                                "listingType": "VENTA",
                                "saleStatus": "RESERVADO",
                                "imageUrl": "https://ejemplo.com/libro.jpg"
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de actualización inválidos",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ningún artículo con el ID proporcionado",
            content = @Content)
      })
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @PutMapping("/{itemId}")
  public ResponseEntity<AcademicItemDTO> updateAcademicItem(
      @Parameter(description = "ID único del artículo académico a actualizar", example = "1")
          @PathVariable
          Integer itemId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Campos actualizados del artículo académico",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = UpdateAcademicItemDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Rebajar y Reservar Libro",
                            summary = "Actualización de precio y estado de venta",
                            value =
                                """
                                {
                                    "categoryId": 1,
                                    "title": "Libro de Programación",
                                    "description": "Apenas usado",
                                    "price": 10.00,
                                    "itemCondition": "COMO_NUEVO",
                                    "saleStatus": "RESERVADO",
                                    "imageUrl": "https://ejemplo.com/libro.jpg"
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          UpdateAcademicItemDTO updateAcademicItemDTO) {
    return ResponseEntity.ok(
        academicItemInConverter.toDto(
            updateAcademicItemUseCase.updateAcademicItem(
                itemId, academicItemInConverter.toDomainUpdate(updateAcademicItemDTO))));
  }

  @Operation(
      summary = "Eliminar un artículo académico",
      description =
          "Elimina permanentemente un artículo académico por su ID. Requiere rol USER o ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "Artículo eliminado correctamente (sin contenido)",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ningún artículo con el ID proporcionado",
            content = @Content)
      })
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @DeleteMapping("/{itemId}")
  public ResponseEntity<Void> deleteAcademicItem(
      @Parameter(description = "ID único del artículo académico a eliminar", example = "1")
          @PathVariable
          Integer itemId) {
    deleteAcademicItemUseCase.deleteAcademicItem(itemId);
    return ResponseEntity.noContent().build();
  }
}
