package org.vedruna.palcole.domain.ports.inbound.messages;

public interface DeleteMessageUseCase {
  void deleteMessage(Integer messageId);
}
