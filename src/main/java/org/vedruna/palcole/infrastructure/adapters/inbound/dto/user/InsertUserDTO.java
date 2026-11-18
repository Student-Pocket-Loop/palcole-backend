package org.vedruna.palcole.infrastructure.adapters.inbound.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InsertUserDTO {

  @NotBlank(message = "El nombre no puede estar vacio")
  @Size(max = 100, message = "El texto no puede superar los 100 caracteres")
  String name;
}
