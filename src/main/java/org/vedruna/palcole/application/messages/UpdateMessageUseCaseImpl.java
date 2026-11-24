package org.vedruna.palcole.application.messages;

import org.vedruna.palcole.domain.model.Message;
import org.vedruna.palcole.domain.ports.inbound.messages.UpdateMessageUseCase;
import org.vedruna.palcole.domain.ports.outbound.MessageStorage;

public class UpdateMessageUseCaseImpl implements UpdateMessageUseCase {

  private final MessageStorage messageStorage;

  public UpdateMessageUseCaseImpl(MessageStorage messageStorage) {
    this.messageStorage = messageStorage;
  }

  @Override
  public Message updateMessage(Integer messageId, Message newMessage) {
    return messageStorage.updateMessage(messageId, newMessage);
  }
}
