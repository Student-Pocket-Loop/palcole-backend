package org.vedruna.palcole.infrastructure.adapters.inbound.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Datos requeridos para crear o actualizar un rol del sistema")
@Data
public class InsertRoleDTO {

  @Schema(description = "Nombre único del rol (máximo 100 caracteres)", example = "ROLE_MODERATOR")
  @NotBlank(message = "El nombre no puede estar vacio")
  @Size(max = 100, message = "El texto no puede superar los 100 caracteres")
  String name;
}
