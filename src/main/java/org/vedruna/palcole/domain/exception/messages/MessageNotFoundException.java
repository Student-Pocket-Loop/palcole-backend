package org.vedruna.palcole.domain.exception.messages;

import org.vedruna.palcole.domain.exception.ResourceNotFoundException;

/** Excepción de dominio que indica que no existe el mensaje solicitado/a. */
public class MessageNotFoundException extends ResourceNotFoundException {

  public MessageNotFoundException(Integer messageId) {
    super("No se encuentra el mensaje con id: " + messageId);
  }
}
