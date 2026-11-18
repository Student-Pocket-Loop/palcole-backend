package org.vedruna.palcole.infrastructure.adapters.outbound.exception;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {

  public UserNotFoundException() {
    super("No se encuentra usuario con dicho id");
  }

  public UserNotFoundException(String message) {
    super(message);
  }
}
