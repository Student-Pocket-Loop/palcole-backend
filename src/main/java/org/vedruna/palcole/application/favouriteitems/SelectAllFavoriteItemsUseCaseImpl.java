package org.vedruna.palcole.application.favouriteitems;

import org.vedruna.palcole.domain.model.FavoriteItem;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.favouriteitems.SelectAllFavoriteItemsUseCase;
import org.vedruna.palcole.domain.ports.outbound.FavoriteItemStorage;

public class SelectAllFavoriteItemsUseCaseImpl implements SelectAllFavoriteItemsUseCase {

  private final FavoriteItemStorage favoriteItemStorage;

  public SelectAllFavoriteItemsUseCaseImpl(FavoriteItemStorage favoriteItemStorage) {
    this.favoriteItemStorage = favoriteItemStorage;
  }

  @Override
  public PageModel<FavoriteItem> selectAllFavoriteItems(PageableModel pageableModel) {
    return favoriteItemStorage.selectAllFavoriteItems(pageableModel);
  }
}
