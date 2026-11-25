package org.vedruna.palcole.application.conversations;

import org.vedruna.palcole.domain.model.Conversation;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.conversations.SelectAllConversationsUseCase;
import org.vedruna.palcole.domain.ports.outbound.ConversationStorage;

public class SelectAllConversationsUseCaseImpl implements SelectAllConversationsUseCase {

  private final ConversationStorage conversationStorage;

  public SelectAllConversationsUseCaseImpl(ConversationStorage conversationStorage) {
    this.conversationStorage = conversationStorage;
  }

  @Override
  public PageModel<Conversation> selectAllConversations(PageableModel pageableModel) {
    return conversationStorage.selectAllConversations(pageableModel);
  }
}
