package org.vedruna.palcole.domain.exception.users;

/** Excepción lanzada cuando una cuenta supera el máximo de intentos fallidos de login. */
public class TooManyLoginAttemptsException extends RuntimeException {

  public TooManyLoginAttemptsException(String message) {
    super(message);
  }
}
