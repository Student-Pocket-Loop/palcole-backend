package org.vedruna.palcole.application.messages;

import org.vedruna.palcole.domain.model.Message;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.messages.SelectAllMessagesUseCase;
import org.vedruna.palcole.domain.ports.outbound.MessageStorage;

public class SelectAllMessagesUseCaseImpl implements SelectAllMessagesUseCase {

  private final MessageStorage messageStorage;

  public SelectAllMessagesUseCaseImpl(MessageStorage messageStorage) {
    this.messageStorage = messageStorage;
  }

  @Override
  public PageModel<Message> selectAllMessages(PageableModel pageableModel) {
    return messageStorage.selectAllMessages(pageableModel);
  }
}
