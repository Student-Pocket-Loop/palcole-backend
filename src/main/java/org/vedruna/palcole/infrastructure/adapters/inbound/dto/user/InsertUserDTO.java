package org.vedruna.palcole.infrastructure.adapters.inbound.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Datos requeridos para crear o actualizar una cuenta de usuario")
@Data
public class InsertUserDTO {

  @Schema(
      description = "Nombre de usuario único en la plataforma (máximo 100 caracteres)",
      example = "juan.perez")
  @NotBlank(message = "El nombre de usuario no puede estar vacio")
  @Size(max = 100, message = "El nombre de usuario no puede superar los 100 caracteres")
  String username;

  @Schema(
      description = "Correo electrónico válido del usuario (máximo 150 caracteres)",
      example = "juan.perez@vedruna.es",
      format = "email")
  @Email
  @NotBlank(message = "El email no puede estar vacio")
  @Size(max = 150, message = "El email no puede superar los 150 caracteres")
  String email;

  @Schema(
      description = "Contraseña de acceso del usuario (máximo 255 caracteres)",
      example = "P@ssw0rdSegura2026",
      format = "password")
  @NotBlank(message = "La contraseña no puede estar vacio")
  @Size(max = 255, message = "La contraseña no puede superar los 255 caracteres")
  String password;
}
