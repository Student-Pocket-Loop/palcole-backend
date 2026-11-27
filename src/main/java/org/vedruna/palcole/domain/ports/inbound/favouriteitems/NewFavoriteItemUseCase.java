package org.vedruna.palcole.domain.ports.inbound.favouriteitems;

import org.vedruna.palcole.domain.model.FavoriteItem;

public interface NewFavoriteItemUseCase {
  FavoriteItem newFavoriteItem(FavoriteItem newFavoriteItem);
}
