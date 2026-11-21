package org.vedruna.palcole.application.conversations;

import org.vedruna.palcole.domain.ports.inbound.conversations.DeleteConversationUseCase;
import org.vedruna.palcole.domain.ports.outbound.ConversationStorage;

public class DeleteConversationUseCaseImpl implements DeleteConversationUseCase {

  private final ConversationStorage conversationStorage;

  public DeleteConversationUseCaseImpl(ConversationStorage conversationStorage) {
    this.conversationStorage = conversationStorage;
  }

  @Override
  public void deleteConversation(Integer conversationId) {
    conversationStorage.deleteConversation(conversationId);
  }
}
