package org.vedruna.palcole.application.favouriteitems;

import org.vedruna.palcole.domain.model.FavoriteItem;
import org.vedruna.palcole.domain.ports.inbound.favouriteitems.NewFavoriteItemUseCase;
import org.vedruna.palcole.domain.ports.outbound.FavoriteItemStorage;

public class NewFavoriteItemUseCaseImpl implements NewFavoriteItemUseCase {

  private final FavoriteItemStorage favoriteItemStorage;

  public NewFavoriteItemUseCaseImpl(FavoriteItemStorage favoriteItemStorage) {
    this.favoriteItemStorage = favoriteItemStorage;
  }

  @Override
  public FavoriteItem newFavoriteItem(FavoriteItem newFavoriteItem) {
    return favoriteItemStorage.insertFavoriteItem(newFavoriteItem);
  }
}
