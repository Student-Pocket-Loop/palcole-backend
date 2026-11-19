package org.vedruna.palcole.domain.exception.users;

import java.util.NoSuchElementException;

/**
 * - Excepción de dominio que indica que no existe el usuario por defecto. No es un error del
 * cliente: - significa que faltan datos iniciales en la base de datos.
 */
public class DefaultUserNotFoundException extends NoSuchElementException {

  public DefaultUserNotFoundException(String username) {
    super("No existe el usuario por defecto '" + username + "' en la base de datos");
  }
}
