package org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.vedruna.palcole.domain.model.enums.ItemCondition;
import org.vedruna.palcole.domain.model.enums.ListingType;
import org.vedruna.palcole.domain.model.enums.SaleStatus;

@Schema(description = "Representación de un artículo académico publicado en la plataforma")
@Data
public class AcademicItemDTO {

  @Schema(
      description = "Identificador único del artículo académico",
      example = "10",
      accessMode = Schema.AccessMode.READ_ONLY)
  Integer itemId;

  @Schema(description = "Identificador del estudiante propietario del anuncio", example = "5")
  Integer studentId;

  @Schema(description = "Identificador de la categoría académica a la que pertenece", example = "2")
  Integer categoryId;

  @Schema(
      description = "Título descriptivo del artículo",
      example = "Libro Matemáticas 2º Bachillerato SM")
  String title;

  @Schema(
      description = "Descripción detallada del estado, edición o contenido del artículo",
      example = "En muy buen estado, sin subrayar y con el código digital sin usar.")
  String description;

  @Schema(
      description = "Precio del artículo en euros (0.00 si es donación o intercambio)",
      example = "18.50")
  BigDecimal price;

  @Schema(description = "Estado físico de conservación del artículo")
  ItemCondition itemCondition;

  @Schema(description = "Tipo de publicación (venta, intercambio, donación, etc.)")
  ListingType listingType;

  @Schema(description = "Estado actual de disponibilidad del anuncio")
  SaleStatus saleStatus;

  @Schema(
      description = "Fecha y hora de publicación del artículo",
      example = "2026-11-23T19:00:00",
      accessMode = Schema.AccessMode.READ_ONLY)
  LocalDateTime publishedAt;

  @Schema(
      description = "URL de la imagen principal del artículo",
      example = "https://cdn.palcole.es/items/matematicas-2bach.jpg")
  String imageUrl;
}
