package org.vedruna.palcole.domain.ports.outbound;

import org.vedruna.palcole.domain.model.FavoriteItem;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface FavoriteItemStorage {

  PageModel<FavoriteItem> selectAllFavoriteItems(PageableModel pageableModel);

  FavoriteItem findFavoriteItemById(Integer userId, Integer itemId);

  FavoriteItem insertFavoriteItem(FavoriteItem newFavoriteItem);

  void deleteFavoriteItem(Integer userId, Integer itemId);
}
