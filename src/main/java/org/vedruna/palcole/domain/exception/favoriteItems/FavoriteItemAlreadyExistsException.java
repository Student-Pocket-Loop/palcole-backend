package org.vedruna.palcole.domain.exception.favoriteItems;

import org.vedruna.palcole.domain.exception.ResourceAlreadyExistsException;

/** Excepción de dominio que indica que ya existe el favorito de artículo con esa clave. */
public class FavoriteItemAlreadyExistsException extends ResourceAlreadyExistsException {

  public FavoriteItemAlreadyExistsException(Integer userId, Integer itemId) {
    super("Ya existe el favorito de artículo" + " con userId: " + userId + " e itemId: " + itemId);
  }
}
