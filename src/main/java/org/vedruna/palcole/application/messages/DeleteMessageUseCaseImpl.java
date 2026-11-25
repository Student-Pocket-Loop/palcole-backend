package org.vedruna.palcole.application.messages;

import org.vedruna.palcole.domain.ports.inbound.messages.DeleteMessageUseCase;
import org.vedruna.palcole.domain.ports.outbound.MessageStorage;

public class DeleteMessageUseCaseImpl implements DeleteMessageUseCase {

  private final MessageStorage messageStorage;

  public DeleteMessageUseCaseImpl(MessageStorage messageStorage) {
    this.messageStorage = messageStorage;
  }

  @Override
  public void deleteMessage(Integer messageId) {
    messageStorage.deleteMessage(messageId);
  }
}
