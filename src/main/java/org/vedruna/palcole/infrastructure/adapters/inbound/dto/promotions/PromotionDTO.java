package org.vedruna.palcole.infrastructure.adapters.inbound.dto.promotions;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Schema(description = "Representación de una promoción o descuento registrado en la plataforma")
@Data
public class PromotionDTO {

  @Schema(
      description = "Identificador único de la promoción",
      example = "5",
      accessMode = Schema.AccessMode.READ_ONLY)
  Integer promotionId;

  @Schema(description = "Identificador de la categoría asociada a la promoción", example = "1")
  Integer categoryId;

  @Schema(
      description = "Título de la promoción",
      example = "20% de descuento en mochilas escolares")
  String title;

  @Schema(
      description = "Descripción detallada de la promoción y sus condiciones",
      example = "Oferta válida para mochilas seleccionadas hasta fin de existencias.")
  String description;

  @Schema(description = "Precio original antes del descuento", example = "45.00")
  BigDecimal originalPrice;

  @Schema(description = "Precio final rebajado tras aplicar la promoción", example = "36.00")
  BigDecimal discountPrice;

  @Schema(description = "Porcentaje de descuento aplicado", example = "20")
  Integer discountPercentage;

  @Schema(description = "Código promocional para canjear el descuento", example = "VUELTAALCOLE20")
  String promoCode;

  @Schema(description = "Fecha de inicio de vigencia de la promoción", example = "2026-09-01")
  LocalDate startDate;

  @Schema(description = "Fecha de finalización de la promoción", example = "2026-09-30")
  LocalDate endDate;
}
