package org.vedruna.palcole.application.favouriteitems;

import org.vedruna.palcole.domain.ports.inbound.favouriteitems.DeleteFavoriteItemUseCase;
import org.vedruna.palcole.domain.ports.outbound.FavoriteItemStorage;

public class DeleteFavoriteItemUseCaseImpl implements DeleteFavoriteItemUseCase {

  private final FavoriteItemStorage favoriteItemStorage;

  public DeleteFavoriteItemUseCaseImpl(FavoriteItemStorage favoriteItemStorage) {
    this.favoriteItemStorage = favoriteItemStorage;
  }

  @Override
  public void deleteFavoriteItem(Integer userId, Integer itemId) {
    favoriteItemStorage.deleteFavoriteItem(userId, itemId);
  }
}
