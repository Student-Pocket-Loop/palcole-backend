package org.vedruna.palcole.domain.exception.promotions;

import org.vedruna.palcole.domain.exception.ResourceNotFoundException;

/** Excepción de dominio que indica que no existe la promoción solicitado/a. */
public class PromotionNotFoundException extends ResourceNotFoundException {

  public PromotionNotFoundException(Integer promotionId) {
    super("No se encuentra la promoción con id: " + promotionId);
  }
}
