package org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems;

import io.swagger.v3.oas.annotations.media.Schema;
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

@Schema(description = "Datos requeridos para actualizar un artículo académico existente")
@Data
public class UpdateAcademicItemDTO {

  @Schema(description = "Identificador de la categoría académica del artículo", example = "2")
  @NotNull(message = "El campo es obligatorio")
  Integer categoryId;

  @Schema(
      description = "Título actualizado del artículo (máximo 150 caracteres)",
      example = "Libro Matemáticas 2º Bachillerato SM - Edición revisada")
  @NotBlank(message = "El campo no puede estar vacio")
  @Size(max = 150, message = "El texto no puede superar los 150 caracteres")
  String title;

  @Schema(
      description = "Descripción actualizada del estado y características del artículo",
      example = "En muy buen estado, forrado y sin anotaciones.")
  @NotBlank(message = "El campo no puede estar vacio")
  String description;

  @Schema(description = "Nuevo precio del artículo en euros (mínimo 0.00)", example = "15.00")
  @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
  @Digits(integer = 6, fraction = 2, message = "Formato de precio no válido")
  BigDecimal price;

  @Schema(description = "Estado físico actualizado de conservación del artículo")
  @NotNull(message = "El campo es obligatorio")
  ItemCondition itemCondition;

  @Schema(description = "Modalidad de publicación actualizada del artículo")
  ListingType listingType;

  @Schema(description = "Estado actualizado de venta o disponibilidad del anuncio")
  SaleStatus saleStatus;

  @Schema(
      description = "Nueva URL de la imagen del artículo (máximo 150 caracteres)",
      example = "https://cdn.palcole.es/items/matematicas-2bach-v2.jpg")
  @NotBlank(message = "El campo no puede estar vacio")
  @Size(max = 150, message = "El texto no puede superar los 150 caracteres")
  String imageUrl;
}
