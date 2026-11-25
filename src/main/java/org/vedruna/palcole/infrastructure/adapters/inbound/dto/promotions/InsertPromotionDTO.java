package org.vedruna.palcole.infrastructure.adapters.inbound.dto.promotions;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Schema(description = "Datos requeridos para crear o actualizar una promoción")
@Data
public class InsertPromotionDTO {

  @Schema(description = "Identificador de la categoría asociada a la promoción", example = "1")
  @NotNull(message = "El campo es obligatorio")
  Integer categoryId;

  @Schema(
      description = "Título llamativo de la promoción (máximo 150 caracteres)",
      example = "20% de descuento en mochilas escolares")
  @NotBlank(message = "El campo no puede estar vacio")
  @Size(max = 150, message = "El texto no puede superar los 150 caracteres")
  String title;

  @Schema(
      description = "Descripción detallada de las condiciones de la promoción",
      example = "Oferta válida para mochilas seleccionadas hasta fin de existencias.")
  String description;

  @Schema(description = "Precio original del artículo antes del descuento", example = "45.00")
  @NotNull(message = "El campo es obligatorio")
  @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
  @Digits(integer = 6, fraction = 2, message = "Formato de precio no válido")
  BigDecimal originalPrice;

  @Schema(description = "Precio final rebajado tras aplicar la promoción", example = "36.00")
  @NotNull(message = "El campo es obligatorio")
  @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
  @Digits(integer = 6, fraction = 2, message = "Formato de precio no válido")
  BigDecimal discountPrice;

  @Schema(description = "Porcentaje de descuento aplicado (entre 0 y 100)", example = "20")
  @NotNull(message = "El campo es obligatorio")
  @Min(value = 0, message = "El porcentaje no puede ser negativo")
  @Max(value = 100, message = "El porcentaje no puede superar 100")
  Integer discountPercentage;

  @Schema(
      description = "Código promocional opcional para canjear el descuento (máximo 50 caracteres)",
      example = "VUELTAALCOLE20")
  @Size(max = 50, message = "El texto no puede superar los 50 caracteres")
  String promoCode;

  @Schema(description = "Fecha de inicio de vigencia de la promoción", example = "2026-09-01")
  @NotNull(message = "El campo es obligatorio")
  LocalDate startDate;

  @Schema(description = "Fecha de finalización de la promoción", example = "2026-09-30")
  @NotNull(message = "El campo es obligatorio")
  LocalDate endDate;
}
