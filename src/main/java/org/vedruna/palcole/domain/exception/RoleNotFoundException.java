package org.vedruna.palcole.domain.exception;

import java.util.NoSuchElementException;

/**
 * Excepción de dominio que indica que el role solicitado no existe.
 *
 * <p>Pertenece al dominio (no depende de ningún framework), así que la pueden lanzar tanto los
 * casos de uso como los adaptadores que implementan los puertos de salida.
 */
public class RoleNotFoundException extends NoSuchElementException {

  public RoleNotFoundException(Integer roleId) {
    super("No se encuentra el role con id: " + roleId);
  }
}
