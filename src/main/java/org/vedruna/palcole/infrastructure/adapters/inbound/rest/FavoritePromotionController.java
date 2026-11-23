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
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.DeleteFavoritePromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.NewFavoritePromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.SelectAllFavoritePromotionsUseCase;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.SelectFavoritePromotionByIdUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.FavoritePromotionInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.favoritePromotions.FavoritePromotionDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.favoritePromotions.InsertFavoritePromotionDTO;

@Tag(
    name = "Favorite Promotions",
    description = "Gestión de promociones marcadas como favoritas por los usuarios")
@CrossOrigin
@AllArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/v1/favorite-promotions")
public class FavoritePromotionController {

  private final NewFavoritePromotionUseCase newFavoritePromotionUseCase;
  private final DeleteFavoritePromotionUseCase deleteFavoritePromotionUseCase;
  private final SelectAllFavoritePromotionsUseCase selectAllFavoritePromotionsUseCase;
  private final SelectFavoritePromotionByIdUseCase selectFavoritePromotionByIdUseCase;
  private final FavoritePromotionInConverter favoritePromotionInConverter;

  @Operation(
      summary = "Listar todas las promociones favoritas (paginado)",
      description =
          "Devuelve una página con las relaciones de promociones favoritas registradas. Requiere rol USER o ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista paginada de promociones favoritas recuperada correctamente"),
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
  public ResponseEntity<Page<FavoritePromotionDTO>> getAllFavoritePromotions(
      @ParameterObject Pageable pageable) {
    PageableModel pageableModel = favoritePromotionInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        favoritePromotionInConverter.toFavoritePromotionsPage(
            selectAllFavoritePromotionsUseCase.selectAllFavoritePromotions(pageableModel),
            pageableModel));
  }

  @Operation(
      summary = "Añadir una promoción a favoritos",
      description =
          "Guarda una promoción como favorita para un usuario específico. Requiere rol USER o ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Promoción añadida a favoritos exitosamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FavoritePromotionDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Favorito guardado (Usuario Normal - manuel)",
                          summary = "Respuesta tras guardar promoción con ROLE_USER",
                          value =
                              """
                              {
                                "userId": 2,
                                "promotionId": 1,
                                "savedAt": "2026-09-01T14:20:00"
                              }
                              """),
                      @ExampleObject(
                          name = "Favorito guardado (Administrador - joaquin)",
                          summary = "Respuesta tras guardar promoción con ROLE_ADMIN",
                          value =
                              """
                              {
                                "userId": 1,
                                "promotionId": 1,
                                "savedAt": "2026-09-01T14:25:00"
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
            content = @Content),
        @ApiResponse(
            responseCode = "409",
            description = "La promoción ya está marcada como favorita por este usuario",
            content = @Content)
      })
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @PostMapping("/")
  public ResponseEntity<FavoritePromotionDTO> insertIntoFavoritePromotion(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Identificadores del usuario y de la promoción a guardar",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = InsertFavoritePromotionDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Guardar Favorito (Usuario Normal)",
                            summary = "Asociar promoción 1 al usuario manuel (ID 2)",
                            value =
                                """
                                {
                                    "userId": 2,
                                    "promotionId": 1
                                }
                                """),
                        @ExampleObject(
                            name = "Guardar Favorito (Admin)",
                            summary = "Asociar promoción 1 al administrador joaquin (ID 1)",
                            value =
                                """
                                {
                                    "userId": 1,
                                    "promotionId": 1
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          InsertFavoritePromotionDTO insertFavoritePromotionDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            favoritePromotionInConverter.toDto(
                newFavoritePromotionUseCase.newFavoritePromotion(
                    favoritePromotionInConverter.toDomainInsert(insertFavoritePromotionDTO))));
  }

  @Operation(
      summary = "Obtener una promoción favorita por usuario y promoción",
      description =
          "Busca y devuelve los detalles de una promoción favorita utilizando el ID del usuario y el ID de la promoción. Requiere rol USER o ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Promoción favorita encontrada",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FavoritePromotionDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Favorito de Usuario Normal (manuel)",
                          summary = "Detalle de promoción favorita del usuario 2",
                          value =
                              """
                              {
                                "userId": 2,
                                "promotionId": 1,
                                "savedAt": "2026-09-01T14:20:00"
                              }
                              """),
                      @ExampleObject(
                          name = "Favorito de Administrador (joaquin)",
                          summary = "Detalle de promoción favorita del usuario 1",
                          value =
                              """
                              {
                                "userId": 1,
                                "promotionId": 1,
                                "savedAt": "2026-09-01T14:25:00"
                              }
                              """)
                    })),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol USER o ADMIN)",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe esa promoción favorita para el usuario indicado",
            content = @Content)
      })
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @GetMapping("/{userId}/{promotionId}")
  public ResponseEntity<FavoritePromotionDTO> getFavoritePromotionById(
      @Parameter(description = "ID único del usuario", example = "1") @PathVariable Integer userId,
      @Parameter(description = "ID único de la promoción", example = "1") @PathVariable
          Integer promotionId) {
    return ResponseEntity.ok(
        favoritePromotionInConverter.toDto(
            selectFavoritePromotionByIdUseCase.selectFavoritePromotionById(userId, promotionId)));
  }

  @Operation(
      summary = "Eliminar una promoción de favoritos",
      description =
          "Elimina una promoción de la lista de favoritos de un usuario mediante sus respectivos IDs. Requiere rol USER o ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "Promoción eliminada de favoritos correctamente (sin contenido)",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol USER o ADMIN)",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe esa promoción favorita para el usuario indicado",
            content = @Content)
      })
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @DeleteMapping("/{userId}/{promotionId}")
  public ResponseEntity<Void> deleteFavoritePromotion(
      @Parameter(description = "ID único del usuario", example = "1") @PathVariable Integer userId,
      @Parameter(description = "ID único de la promoción", example = "1") @PathVariable
          Integer promotionId) {
    deleteFavoritePromotionUseCase.deleteFavoritePromotion(userId, promotionId);
    return ResponseEntity.noContent().build();
  }
}
