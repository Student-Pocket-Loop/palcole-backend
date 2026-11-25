package org.vedruna.palcole.domain.ports.inbound.messages;

import org.vedruna.palcole.domain.model.Message;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface SelectAllMessagesUseCase {
  PageModel<Message> selectAllMessages(PageableModel pageableModel);
}
