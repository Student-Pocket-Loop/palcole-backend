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
import org.vedruna.palcole.domain.ports.inbound.promotions.DeletePromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.NewPromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.SelectAllPromotionsUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.SelectPromotionByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.UpdatePromotionUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.PromotionInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.promotions.InsertPromotionDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.promotions.PromotionDTO;

@Tag(
    name = "Promotions",
    description = "Gestión de promociones y descuentos disponibles en la plataforma")
@CrossOrigin
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController {

  private final NewPromotionUseCase newPromotionUseCase;
  private final DeletePromotionUseCase deletePromotionUseCase;
  private final SelectAllPromotionsUseCase selectAllPromotionsUseCase;
  private final SelectPromotionByIdUseCase selectPromotionByIdUseCase;
  private final UpdatePromotionUseCase updatePromotionUseCase;
  private final PromotionInConverter promotionInConverter;

  @Operation(
      summary = "Listar todas las promociones (paginado)",
      description =
          "Devuelve una página con las promociones registradas. Permite paginación y ordenación mediante parámetros query.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista paginada de promociones recuperada correctamente"),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de paginación u ordenación inválidos",
            content = @Content)
      })
  @GetMapping("/")
  public ResponseEntity<Page<PromotionDTO>> getAllPromotions(@ParameterObject Pageable pageable) {
    PageableModel pageableModel = promotionInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        promotionInConverter.toPromotionsPage(
            selectAllPromotionsUseCase.selectAllPromotions(pageableModel), pageableModel));
  }

  @Operation(
      summary = "Crear una nueva promoción",
      description = "Registra una nueva promoción en el sistema. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Promoción creada exitosamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PromotionDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Promoción creada (ADMIN)",
                          summary = "Respuesta tras registrar Mochilas escolares con ROLE_ADMIN",
                          value =
                              """
                              {
                                "promotionId": 3,
                                "categoryId": 3,
                                "title": "Mochilas escolares",
                                "description": "Descuento en mochilas",
                                "originalPrice": 40.00,
                                "discountPrice": 30.00,
                                "discountPercentage": 25,
                                "promoCode": "MOCHI25",
                                "startDate": "2026-09-01",
                                "endDate": "2026-10-15"
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
            description = "No autorizado (se requiere rol ADMIN, denegado para ROLE_USER)",
            content = @Content)
      })
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/")
  public ResponseEntity<PromotionDTO> insertIntoPromotion(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Datos de la nueva promoción",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = InsertPromotionDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Mochilas escolares (MOCHI25)",
                            summary = "Ejemplo de creación de promoción",
                            value =
                                """
                                {
                                    "categoryId": 3,
                                    "title": "Mochilas escolares",
                                    "description": "Descuento en mochilas",
                                    "originalPrice": 40.00,
                                    "discountPrice": 30.00,
                                    "discountPercentage": 25,
                                    "promoCode": "MOCHI25",
                                    "startDate": "2026-09-01",
                                    "endDate": "2026-10-15"
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          InsertPromotionDTO insertPromotionDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            promotionInConverter.toDto(
                newPromotionUseCase.newPromotion(
                    promotionInConverter.toDomainInsert(insertPromotionDTO))));
  }

  @Operation(
      summary = "Obtener una promoción por su ID",
      description =
          "Busca y devuelve los detalles de una promoción específica a partir de su identificador.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Promoción encontrada",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PromotionDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Detalle de Promoción",
                          summary = "Respuesta al consultar promoción registrada",
                          value =
                              """
                              {
                                "promotionId": 3,
                                "categoryId": 3,
                                "title": "Mochilas escolares",
                                "description": "Descuento en mochilas",
                                "originalPrice": 40.00,
                                "discountPrice": 30.00,
                                "discountPercentage": 25,
                                "promoCode": "MOCHI25",
                                "startDate": "2026-09-01",
                                "endDate": "2026-10-15"
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ninguna promoción con el ID proporcionado",
            content = @Content)
      })
  @GetMapping("/{promotionId}")
  public ResponseEntity<PromotionDTO> getPromotionById(
      @Parameter(description = "ID único de la promoción", example = "1") @PathVariable
          Integer promotionId) {
    return ResponseEntity.ok(
        promotionInConverter.toDto(selectPromotionByIdUseCase.selectPromotionById(promotionId)));
  }

  @Operation(
      summary = "Actualizar una promoción",
      description = "Modifica los datos de una promoción existente. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Promoción actualizada correctamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PromotionDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Promoción ampliada (ADMIN)",
                          summary = "Respuesta tras actualizar descuento a MOCHI30",
                          value =
                              """
                              {
                                "promotionId": 3,
                                "categoryId": 3,
                                "title": "Mochilas escolares (ampliada)",
                                "description": "Descuento en mochilas",
                                "originalPrice": 40.00,
                                "discountPrice": 28.00,
                                "discountPercentage": 30,
                                "promoCode": "MOCHI30",
                                "startDate": "2026-09-01",
                                "endDate": "2026-10-31"
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de actualización inválidos",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol ADMIN, denegado para ROLE_USER)",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ninguna promoción con el ID proporcionado",
            content = @Content)
      })
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{promotionId}")
  public ResponseEntity<PromotionDTO> updatePromotion(
      @Parameter(description = "ID único de la promoción a actualizar", example = "3") @PathVariable
          Integer promotionId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Datos actualizados de la promoción",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = InsertPromotionDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Ampliar Promoción (MOCHI30)",
                            summary = "Ejemplo de actualización de descuento y fecha",
                            value =
                                """
                                {
                                    "categoryId": 3,
                                    "title": "Mochilas escolares (ampliada)",
                                    "description": "Descuento en mochilas",
                                    "originalPrice": 40.00,
                                    "discountPrice": 28.00,
                                    "discountPercentage": 30,
                                    "promoCode": "MOCHI30",
                                    "startDate": "2026-09-01",
                                    "endDate": "2026-10-31"
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          InsertPromotionDTO insertPromotionDTO) {
    return ResponseEntity.ok(
        promotionInConverter.toDto(
            updatePromotionUseCase.updatePromotion(
                promotionId, promotionInConverter.toDomainInsert(insertPromotionDTO))));
  }

  @Operation(
      summary = "Eliminar una promoción",
      description = "Elimina permanentemente una promoción por su ID. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "Promoción eliminada correctamente (sin contenido)",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol ADMIN)",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ninguna promoción con el ID proporcionado",
            content = @Content)
      })
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{promotionId}")
  public ResponseEntity<Void> deletePromotion(
      @Parameter(description = "ID único de la promoción a eliminar", example = "3") @PathVariable
          Integer promotionId) {
    deletePromotionUseCase.deletePromotion(promotionId);
    return ResponseEntity.noContent().build();
  }
}
