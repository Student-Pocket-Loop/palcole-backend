package org.vedruna.palcole.domain.ports.inbound.conversations;

import org.vedruna.palcole.domain.model.Conversation;

public interface SelectConversationByIdUseCase {
  Conversation selectConversationById(Integer conversationId);
}
