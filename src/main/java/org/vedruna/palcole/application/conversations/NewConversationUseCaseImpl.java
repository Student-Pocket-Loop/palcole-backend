package org.vedruna.palcole.application.conversations;

import java.time.LocalDateTime;
import org.vedruna.palcole.domain.model.Conversation;
import org.vedruna.palcole.domain.ports.inbound.conversations.NewConversationUseCase;
import org.vedruna.palcole.domain.ports.outbound.ConversationStorage;

public class NewConversationUseCaseImpl implements NewConversationUseCase {

  private final ConversationStorage conversationStorage;

  public NewConversationUseCaseImpl(ConversationStorage conversationStorage) {
    this.conversationStorage = conversationStorage;
  }

  @Override
  public Conversation newConversation(Conversation newConversation) {
    newConversation.setStartedAt(LocalDateTime.now());
    return conversationStorage.insertConversation(newConversation);
  }
}
