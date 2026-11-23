package org.vedruna.palcole.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Schema(description = "Información pública y estado de la cuenta de un usuario")
@Data
public class UserDto {

  @Schema(
      description = "Identificador único del usuario",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  Integer userId;

  @Schema(description = "Nombre de usuario único", example = "juan.perez")
  String username;

  @Schema(description = "Correo electrónico del usuario", example = "juan.perez@vedruna.es")
  String email;

  @Schema(
      description = "Fecha y hora de creación de la cuenta",
      example = "2026-11-23T18:30:00",
      accessMode = Schema.AccessMode.READ_ONLY)
  LocalDateTime createDate;

  @Schema(
      description = "Fecha y hora en la que expira la cuenta del usuario",
      example = "2027-11-23T18:30:00")
  LocalDateTime accountExpirationDate;

  @Schema(description = "Indica si la cuenta está bloqueada", example = "false")
  Boolean isLocked;

  @Schema(
      description = "Fecha y hora en la que expiran las credenciales (contraseña) del usuario",
      example = "2027-05-23T18:30:00")
  LocalDateTime credentialsExpirationDate;

  @Schema(
      description = "Indica si la cuenta del usuario está activa y habilitada",
      example = "true")
  Boolean isEnabled;

  @Schema(description = "Nombre del rol asignado al usuario", example = "ROLE_USER")
  String userRolName;
}
