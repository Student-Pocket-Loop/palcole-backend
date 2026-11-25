package org.vedruna.palcole.domain.ports.inbound.conversations;

import org.vedruna.palcole.domain.model.Conversation;

public interface NewConversationUseCase {
  Conversation newConversation(Conversation newConversation);
}
