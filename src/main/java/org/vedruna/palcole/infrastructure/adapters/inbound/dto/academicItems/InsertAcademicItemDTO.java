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

@Schema(description = "Datos requeridos para publicar un nuevo artículo académico")
@Data
public class InsertAcademicItemDTO {

  @Schema(description = "Identificador del estudiante que publica el artículo", example = "5")
  @NotNull(message = "El campo es obligatorio")
  Integer studentId;

  @Schema(description = "Identificador de la categoría académica del artículo", example = "2")
  @NotNull(message = "El campo es obligatorio")
  Integer categoryId;

  @Schema(
      description = "Título descriptivo del artículo (máximo 150 caracteres)",
      example = "Libro Matemáticas 2º Bachillerato SM")
  @NotBlank(message = "El campo no puede estar vacio")
  @Size(max = 150, message = "El texto no puede superar los 150 caracteres")
  String title;

  @Schema(
      description = "Descripción detallada del estado y características del artículo",
      example = "En muy buen estado, sin subrayar y con el código digital sin usar.")
  @NotBlank(message = "El campo no puede estar vacio")
  String description;

  @Schema(description = "Precio del artículo en euros (mínimo 0.00)", example = "18.50")
  @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
  @Digits(integer = 6, fraction = 2, message = "Formato de precio no válido")
  BigDecimal price;

  @Schema(description = "Estado físico de conservación del artículo")
  @NotNull(message = "El campo es obligatorio")
  ItemCondition itemCondition;

  @Schema(description = "Modalidad de publicación del artículo (venta, intercambio, etc.)")
  ListingType listingType;

  @Schema(description = "Estado inicial de venta o disponibilidad del anuncio")
  SaleStatus saleStatus;

  @Schema(
      description = "URL de la imagen del artículo (máximo 150 caracteres)",
      example = "https://cdn.palcole.es/items/matematicas-2bach.jpg")
  @NotBlank(message = "El campo no puede estar vacio")
  @Size(max = 150, message = "El texto no puede superar los 150 caracteres")
  String imageUrl;
}
