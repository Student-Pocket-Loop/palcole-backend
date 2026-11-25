package org.vedruna.palcole.infrastructure.adapters.inbound.dto.messages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InsertMessageDTO {

  @NotNull(message = "El campo es obligatorio")
  Integer conversationId;

  @NotNull(message = "El campo es obligatorio")
  Integer senderId;

  @NotBlank(message = "El mensaje no puede estar vacio")
  String content;
}
