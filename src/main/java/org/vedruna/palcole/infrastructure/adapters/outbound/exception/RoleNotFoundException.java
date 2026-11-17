package org.vedruna.palcole.infrastructure.adapters.outbound.exception;

import java.util.NoSuchElementException;

public class RoleNotFoundException extends NoSuchElementException {

  public RoleNotFoundException() {
    super("No se encuentra alumno con dicho id");
  }

  public RoleNotFoundException(String message) {
    super(message);
  }
}
