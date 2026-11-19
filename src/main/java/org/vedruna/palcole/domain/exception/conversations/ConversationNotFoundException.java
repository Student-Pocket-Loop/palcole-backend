package org.vedruna.palcole.domain.exception.conversations;

import org.vedruna.palcole.domain.exception.ResourceNotFoundException;

/** Excepción de dominio que indica que no existe la conversación solicitado/a. */
public class ConversationNotFoundException extends ResourceNotFoundException {

  public ConversationNotFoundException(Integer conversationId) {
    super("No se encuentra la conversación con id: " + conversationId);
  }
}
