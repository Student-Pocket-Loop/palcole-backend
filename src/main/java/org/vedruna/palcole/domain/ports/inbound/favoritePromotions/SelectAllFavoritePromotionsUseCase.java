package org.vedruna.palcole.domain.ports.inbound.favoritePromotions;

import org.vedruna.palcole.domain.model.FavoritePromotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface SelectAllFavoritePromotionsUseCase {
  PageModel<FavoritePromotion> selectAllFavoritePromotions(PageableModel pageableModel);
}
