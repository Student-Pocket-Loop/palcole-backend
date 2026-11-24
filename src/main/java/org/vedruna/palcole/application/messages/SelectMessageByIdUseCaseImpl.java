package org.vedruna.palcole.application.messages;

import org.vedruna.palcole.domain.model.Message;
import org.vedruna.palcole.domain.ports.inbound.messages.SelectMessageByIdUseCase;
import org.vedruna.palcole.domain.ports.outbound.MessageStorage;

public class SelectMessageByIdUseCaseImpl implements SelectMessageByIdUseCase {

  private final MessageStorage messageStorage;

  public SelectMessageByIdUseCaseImpl(MessageStorage messageStorage) {
    this.messageStorage = messageStorage;
  }

  @Override
  public Message selectMessageById(Integer messageId) {
    return messageStorage.findMessageById(messageId);
  }
}
