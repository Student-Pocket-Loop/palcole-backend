package org.vedruna.palcole.domain.exception.categories;

import org.vedruna.palcole.domain.exception.ResourceNotFoundException;

/** Excepción de dominio que indica que no existe la categoría solicitado/a. */
public class CategoryNotFoundException extends ResourceNotFoundException {

  public CategoryNotFoundException(Integer categoryId) {
    super("No se encuentra la categoría con id: " + categoryId);
  }
}
