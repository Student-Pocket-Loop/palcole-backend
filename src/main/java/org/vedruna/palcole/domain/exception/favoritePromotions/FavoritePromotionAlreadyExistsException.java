package org.vedruna.palcole.domain.exception.favoritePromotions;

import org.vedruna.palcole.domain.exception.ResourceAlreadyExistsException;

/** Excepción de dominio que indica que ya existe el favorito de promoción con esa clave. */
public class FavoritePromotionAlreadyExistsException extends ResourceAlreadyExistsException {

  public FavoritePromotionAlreadyExistsException(Integer userId, Integer promotionId) {
    super(
        "Ya existe el favorito de promoción"
            + " con userId: "
            + userId
            + " e promotionId: "
            + promotionId);
  }
}
