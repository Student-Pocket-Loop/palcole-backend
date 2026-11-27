package org.vedruna.palcole.domain.ports.inbound.favouriteitems;

import org.vedruna.palcole.domain.model.FavoriteItem;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface SelectAllFavoriteItemsUseCase {
  PageModel<FavoriteItem> selectAllFavoriteItems(PageableModel pageableModel);
}
