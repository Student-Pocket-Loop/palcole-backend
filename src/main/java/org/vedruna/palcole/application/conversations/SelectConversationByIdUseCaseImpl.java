package org.vedruna.palcole.application.conversations;

import org.vedruna.palcole.domain.model.Conversation;
import org.vedruna.palcole.domain.ports.inbound.conversations.SelectConversationByIdUseCase;
import org.vedruna.palcole.domain.ports.outbound.ConversationStorage;

public class SelectConversationByIdUseCaseImpl implements SelectConversationByIdUseCase {

  private final ConversationStorage conversationStorage;

  public SelectConversationByIdUseCaseImpl(ConversationStorage conversationStorage) {
    this.conversationStorage = conversationStorage;
  }

  @Override
  public Conversation selectConversationById(Integer conversationId) {
    return conversationStorage.findConversationById(conversationId);
  }
}
