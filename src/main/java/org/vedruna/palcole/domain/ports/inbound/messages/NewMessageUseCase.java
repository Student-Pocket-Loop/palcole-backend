package org.vedruna.palcole.domain.ports.inbound.messages;

import org.vedruna.palcole.domain.model.Message;

public interface NewMessageUseCase {
  Message newMessage(Message newMessage);
}
