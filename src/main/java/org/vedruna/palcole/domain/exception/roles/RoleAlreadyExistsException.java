package org.vedruna.palcole.domain.exception.roles;

import org.vedruna.palcole.domain.exception.ResourceAlreadyExistsException;

/**
 * Excepción de dominio que indica que ya existe un role con el mismo nombre. Regla de negocio: el
 * nombre de un role debe ser único.
 */
public class RoleAlreadyExistsException extends ResourceAlreadyExistsException {

  public RoleAlreadyExistsException(String name) {
    super("Ya existe un role con el nombre: " + name);
  }
}
