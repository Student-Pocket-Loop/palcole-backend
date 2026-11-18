package org.vedruna.palcole.domain.exception.role;

import java.util.NoSuchElementException;

/**
 * Excepción de dominio que indica que no existe el role por defecto que se asigna a los usuarios al
 * registrarse (normalmente "USER"). No es un error del cliente: significa que faltan datos
 * iniciales en la base de datos.
 */
public class DefaultRoleNotFoundException extends NoSuchElementException {

  public DefaultRoleNotFoundException(String roleName) {
    super("No existe el role por defecto '" + roleName + "' en la base de datos");
  }
}
