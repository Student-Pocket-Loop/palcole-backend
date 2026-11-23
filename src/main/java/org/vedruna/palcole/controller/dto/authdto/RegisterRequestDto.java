package org.vedruna.palcole.controller.dto.authdto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Datos necesarios para registrar una nueva cuenta de usuario")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

  @Schema(description = "Nombre de usuario único para iniciar sesión", example = "nuevo.usuario")
  @NotBlank
  String username;

  @Schema(description = "Contraseña segura para la cuenta", example = "Password123!")
  @NotBlank
  String password;

  @Schema(
      description = "Correo electrónico válido del usuario",
      example = "nuevo.usuario@vedruna.es")
  @Email
  String email;
}
