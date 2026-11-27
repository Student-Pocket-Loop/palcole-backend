package org.vedruna.palcole.application.favouriteitems;

import org.vedruna.palcole.domain.model.FavoriteItem;
import org.vedruna.palcole.domain.ports.inbound.favouriteitems.SelectFavoriteItemByIdUseCase;
import org.vedruna.palcole.domain.ports.outbound.FavoriteItemStorage;

public class SelectFavoriteItemByIdUseCaseImpl implements SelectFavoriteItemByIdUseCase {

  private final FavoriteItemStorage favoriteItemStorage;

  public SelectFavoriteItemByIdUseCaseImpl(FavoriteItemStorage favoriteItemStorage) {
    this.favoriteItemStorage = favoriteItemStorage;
  }

  @Override
  public FavoriteItem selectFavoriteItemById(Integer userId, Integer itemId) {
    return favoriteItemStorage.findFavoriteItemById(userId, itemId);
  }
}
