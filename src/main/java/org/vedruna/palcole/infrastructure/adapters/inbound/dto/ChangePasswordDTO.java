package org.vedruna.palcole.infrastructure.adapters.inbound.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Datos para cambiar la contraseña del usuario autenticado")
@Data
public class ChangePasswordDTO {

  @Schema(description = "Contraseña actual", example = "Abcd1234", format = "password")
  @NotBlank(message = "La contraseña actual no puede estar vacia")
  String currentPassword;

  @Schema(description = "Nueva contraseña (8-255 caracteres)", example = "NuevaPass2026", format = "password")
  @NotBlank(message = "La nueva contraseña no puede estar vacia")
  @Size(min = 8, max = 255, message = "La nueva contraseña debe tener entre 8 y 255 caracteres")
  String newPassword;
}