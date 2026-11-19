package org.vedruna.palcole.infrastructure.adapters.inbound.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InsertUserDTO {

  @NotBlank(message = "El nombre de usuario no puede estar vacio")
  @Size(max = 100, message = "El nombre de usuario no puede superar los 100 caracteres")
  String username;

  @Email
  @NotBlank(message = "El email no puede estar vacio")
  @Size(max = 150, message = "El email no puede superar los 150 caracteres")
  String email;

  @NotBlank(message = "La contraseña no puede estar vacio")
  @Size(max = 255, message = "La contraseña no puede superar los 255 caracteres")
  String password;
}
