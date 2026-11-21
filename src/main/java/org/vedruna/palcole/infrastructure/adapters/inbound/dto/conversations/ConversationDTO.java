package org.vedruna.palcole.infrastructure.adapters.inbound.dto.conversations;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ConversationDTO {

  Integer conversationId;

  Integer itemId;

  Integer buyerId;

  LocalDateTime startedAt;
}
