package org.vedruna.palcole.infrastructure.adapters.inbound.dto.conversations;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InsertConversationDTO {

  @NotNull(message = "El campo es obligatorio")
  Integer itemId;

  @NotNull(message = "El campo es obligatorio")
  Integer buyerId;
}
