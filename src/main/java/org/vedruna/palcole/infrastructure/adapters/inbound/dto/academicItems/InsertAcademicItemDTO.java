package org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import org.vedruna.palcole.domain.model.enums.ItemCondition;
import org.vedruna.palcole.domain.model.enums.ListingType;
import org.vedruna.palcole.domain.model.enums.SaleStatus;

@Data
public class InsertAcademicItemDTO {

  @NotNull(message = "El campo es obligatorio")
  Integer studentId;

  @NotNull(message = "El campo es obligatorio")
  Integer categoryId;

  @NotBlank(message = "El campo no puede estar vacio")
  @Size(max = 150, message = "El texto no puede superar los 150 caracteres")
  String title;

  @NotBlank(message = "El campo no puede estar vacio")
  String description;

  @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
  @Digits(integer = 6, fraction = 2, message = "Formato de precio no válido")
  BigDecimal price;

  @NotNull(message = "El campo es obligatorio")
  ItemCondition itemCondition;

  ListingType listingType;

  SaleStatus saleStatus;

  @NotBlank(message = "El campo no puede estar vacio")
  @Size(max = 150, message = "El texto no puede superar los 150 caracteres")
  String imageUrl;
}
