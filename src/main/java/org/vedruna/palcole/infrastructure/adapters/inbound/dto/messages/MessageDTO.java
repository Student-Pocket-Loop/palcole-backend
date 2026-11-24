package org.vedruna.palcole.infrastructure.adapters.inbound.dto.messages;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MessageDTO {

  Integer messageId;

  Integer conversationId;

  Integer senderId;

  String content;

  LocalDateTime sentAt;
}
