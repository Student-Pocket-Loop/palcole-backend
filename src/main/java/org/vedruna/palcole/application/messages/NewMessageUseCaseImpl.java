package org.vedruna.palcole.application.messages;

import java.time.LocalDateTime;
import org.vedruna.palcole.domain.model.Message;
import org.vedruna.palcole.domain.ports.inbound.messages.NewMessageUseCase;
import org.vedruna.palcole.domain.ports.outbound.MessageStorage;

public class NewMessageUseCaseImpl implements NewMessageUseCase {

  private final MessageStorage messageStorage;

  public NewMessageUseCaseImpl(MessageStorage messageStorage) {
    this.messageStorage = messageStorage;
  }

  @Override
  public Message newMessage(Message newMessage) {
    newMessage.setSentAt(LocalDateTime.now());
    return messageStorage.insertMessage(newMessage);
  }
}
