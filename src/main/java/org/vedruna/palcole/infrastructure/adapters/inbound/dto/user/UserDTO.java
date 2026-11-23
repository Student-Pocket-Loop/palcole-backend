package org.vedruna.palcole.infrastructure.adapters.inbound.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Schema(description = "Representación completa de una cuenta de usuario en el sistema")
@Data
public class UserDTO {

  @Schema(
      description = "Identificador único del usuario",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  Integer userId;

  @Schema(description = "Identificador del rol asignado al usuario", example = "2")
  Integer rolesRoleId;

  @Schema(description = "Nombre de usuario único", example = "juan.perez")
  String username;

  @Schema(
      description = "Correo electrónico registrado del usuario",
      example = "juan.perez@vedruna.es",
      format = "email")
  String email;

  @Schema(
      description = "Contraseña cifrada (hash) del usuario",
      example = "$2a$10$7EqJtq98hPqEX7fNZaFWoO...",
      format = "password",
      accessMode = Schema.AccessMode.WRITE_ONLY)
  String password;

  @Schema(
      description = "Centro educativo al que pertenece el usuario",
      example = "Colegio Santa Joaquina de Vedruna")
  String educationalCenter;

  @Schema(
      description = "Fecha y hora de creación de la cuenta",
      example = "2026-09-01T10:30:00",
      accessMode = Schema.AccessMode.READ_ONLY)
  LocalDateTime createDate;

  @Schema(
      description = "Fecha y hora de expiración de la cuenta (nulo si no expira)",
      example = "2027-09-01T23:59:59")
  LocalDateTime accountExpirationDate;

  @Schema(description = "Indica si la cuenta está bloqueada por seguridad", example = "false")
  Boolean isLocked;

  @Schema(
      description = "Fecha y hora de expiración de las credenciales",
      example = "2027-03-01T23:59:59")
  LocalDateTime credentialsExpirationDate;

  @Schema(description = "Indica si la cuenta de usuario está activa y habilitada", example = "true")
  Boolean isEnabled;
}
