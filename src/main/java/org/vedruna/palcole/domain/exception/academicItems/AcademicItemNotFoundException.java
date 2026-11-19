package org.vedruna.palcole.domain.exception.academicItems;

import org.vedruna.palcole.domain.exception.ResourceNotFoundException;

/** Excepción de dominio que indica que no existe el artículo solicitado/a. */
public class AcademicItemNotFoundException extends ResourceNotFoundException {

  public AcademicItemNotFoundException(Integer itemId) {
    super("No se encuentra el artículo con id: " + itemId);
  }
}
