package org.vedruna.palcole.domain.exception.users;

public class InvalidCurrentPasswordException extends IllegalArgumentException {
  public InvalidCurrentPasswordException() {
    super("La contraseña actual no es correcta");
  }
}
