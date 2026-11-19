package org.vedruna.palcole.domain.exception;

/**
 * Excepción base de dominio para "el recurso ya existe". El manejador global la traduce a una
 * respuesta 409 (Conflict).
 */
public class ResourceAlreadyExistsException extends RuntimeException {

  public ResourceAlreadyExistsException(String message) {
    super(message);
  }
}
