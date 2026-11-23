package org.vedruna.palcole.infrastructure.adapters.inbound.dto.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Datos requeridos para crear o actualizar una categoría académica")
@Data
public class InsertCategoryDTO {

  @Schema(
      description = "Nombre único de la categoría (máximo 100 caracteres)",
      example = "Libros de texto")
  @NotBlank(message = "El nombre no puede estar vacio")
  @Size(max = 100, message = "El texto no puede superar los 100 caracteres")
  String name;
}
