package org.vedruna.palcole.domain.exception;

import java.util.NoSuchElementException;

/**
 * Excepción base de dominio para "el recurso solicitado no existe". El manejador global la traduce
 * a una respuesta 404, de modo que cada tabla solo tiene que extenderla.
 */
public class ResourceNotFoundException extends NoSuchElementException {

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
