package org.vedruna.palcole.domain.ports.outbound;

import org.vedruna.palcole.domain.model.Message;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface MessageStorage {

  PageModel<Message> selectAllMessages(PageableModel pageableModel);

  Message findMessageById(Integer messageId);

  Message updateMessage(Integer messageId, Message newMessage);

  Message insertMessage(Message newMessage);

  void deleteMessage(Integer messageId);
}
