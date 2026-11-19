package org.vedruna.palcole.domain.exception.favoritePromotions;

import org.vedruna.palcole.domain.exception.ResourceNotFoundException;

/** Excepción de dominio que indica que no existe el favorito de promoción solicitado/a. */
public class FavoritePromotionNotFoundException extends ResourceNotFoundException {

  public FavoritePromotionNotFoundException(Integer userId, Integer promotionId) {
    super(
        "No se encuentra el favorito de promoción"
            + " con userId: "
            + userId
            + " e promotionId: "
            + promotionId);
  }
}
