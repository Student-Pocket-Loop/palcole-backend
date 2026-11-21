package org.vedruna.palcole.infrastructure.adapters.inbound.dto.promotions;

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

@Data
public class InsertPromotionDTO {

  @NotNull(message = "El campo es obligatorio")
  Integer categoryId;

  @NotBlank(message = "El campo no puede estar vacio")
  @Size(max = 150, message = "El texto no puede superar los 150 caracteres")
  String title;

  String description;

  @NotNull(message = "El campo es obligatorio")
  @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
  @Digits(integer = 6, fraction = 2, message = "Formato de precio no válido")
  BigDecimal originalPrice;

  @NotNull(message = "El campo es obligatorio")
  @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
  @Digits(integer = 6, fraction = 2, message = "Formato de precio no válido")
  BigDecimal discountPrice;

  @NotNull(message = "El campo es obligatorio")
  @Min(value = 0, message = "El porcentaje no puede ser negativo")
  @Max(value = 100, message = "El porcentaje no puede superar 100")
  Integer discountPercentage;

  @Size(max = 50, message = "El texto no puede superar los 50 caracteres")
  String promoCode;

  @NotNull(message = "El campo es obligatorio")
  LocalDate startDate;

  @NotNull(message = "El campo es obligatorio")
  LocalDate endDate;
}
