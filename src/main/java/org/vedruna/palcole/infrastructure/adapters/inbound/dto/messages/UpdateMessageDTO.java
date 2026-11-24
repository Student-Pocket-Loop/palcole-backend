package org.vedruna.palcole.infrastructure.adapters.inbound.dto.messages;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateMessageDTO {

  @NotBlank(message = "El mensaje no puede estar vacio")
  String content;
}
