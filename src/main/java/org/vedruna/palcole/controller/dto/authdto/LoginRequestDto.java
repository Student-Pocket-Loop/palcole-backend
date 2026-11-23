package org.vedruna.palcole.controller.dto.authdto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Datos requeridos para iniciar sesión en la aplicación")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

  @Schema(
      description = "Nombre de usuario o correo electrónico registrado",
      example = "usuario.ejemplo")
  @NotBlank
  String username;

  @Schema(description = "Contraseña de la cuenta del usuario", example = "Password123!")
  @NotBlank
  String password;
}
