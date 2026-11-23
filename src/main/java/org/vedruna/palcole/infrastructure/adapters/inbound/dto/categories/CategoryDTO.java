package org.vedruna.palcole.infrastructure.adapters.inbound.dto.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Representación de una categoría académica")
@Data
public class CategoryDTO {

  @Schema(
      description = "Identificador único de la categoría",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  Integer categoryId;

  @Schema(description = "Nombre descriptivo de la categoría", example = "Libros de texto")
  String name;
}
