package org.vedruna.palcole.domain.exception.favoriteItems;

import org.vedruna.palcole.domain.exception.ResourceNotFoundException;

/** Excepción de dominio que indica que no existe el favorito de artículo solicitado/a. */
public class FavoriteItemNotFoundException extends ResourceNotFoundException {

  public FavoriteItemNotFoundException(Integer userId, Integer itemId) {
    super(
        "No se encuentra el favorito de artículo"
            + " con userId: "
            + userId
            + " e itemId: "
            + itemId);
  }
}
