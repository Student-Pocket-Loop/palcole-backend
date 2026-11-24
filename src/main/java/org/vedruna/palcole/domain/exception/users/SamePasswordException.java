package org.vedruna.palcole.domain.exception.users;

public class SamePasswordException extends IllegalArgumentException {
  public SamePasswordException() {
    super("La nueva contraseña debe ser distinta de la actual");
  }
}
