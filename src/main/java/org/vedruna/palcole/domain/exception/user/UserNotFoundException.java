package org.vedruna.palcole.domain.exception.user;

import java.util.NoSuchElementException;

/**
 * - Excepción de dominio que indica que el usuario solicitado no existe.
 *
 * <p>-
 *
 * <p>Pertenece al dominio (no depende de ningún framework), así que la pueden lanzar tanto los -
 * casos de uso como los adaptadores que implementan los puertos de salida.
 */
public class UserNotFoundException extends NoSuchElementException {

  public UserNotFoundException(Integer userId) {
    super("No se encuentra el usuario con id: " + userId);
  }
}
