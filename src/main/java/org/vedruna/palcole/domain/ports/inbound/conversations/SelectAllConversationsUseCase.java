package org.vedruna.palcole.domain.ports.inbound.conversations;

import org.vedruna.palcole.domain.model.Conversation;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface SelectAllConversationsUseCase {
  PageModel<Conversation> selectAllConversations(PageableModel pageableModel);
}
