package org.vedruna.palcole.domain.ports.outbound;

import org.vedruna.palcole.domain.model.Conversation;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface ConversationStorage {

  PageModel<Conversation> selectAllConversations(PageableModel pageableModel);

  Conversation findConversationById(Integer conversationId);

  Conversation insertConversation(Conversation newConversation);

  void deleteConversation(Integer conversationId);
}
