package org.vedruna.palcole.domain.exception.user;

/**
 * - Excepción de dominio que indica que ya existe un usuario con el mismo nombre de usuario. Regla
 * - de negocio: el username de un usuario debe ser único.
 */
public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException(String username) {
    super("Ya existe un usuario con el nombre: " + username);
  }
}
