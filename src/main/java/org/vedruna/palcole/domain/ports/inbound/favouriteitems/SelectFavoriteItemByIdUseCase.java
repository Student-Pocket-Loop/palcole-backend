package org.vedruna.palcole.domain.ports.inbound.favouriteitems;

import org.vedruna.palcole.domain.model.FavoriteItem;

public interface SelectFavoriteItemByIdUseCase {
  FavoriteItem selectFavoriteItemById(Integer userId, Integer itemId);
}
