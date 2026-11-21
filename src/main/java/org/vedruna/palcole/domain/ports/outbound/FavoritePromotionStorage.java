package org.vedruna.palcole.domain.ports.outbound;

import org.vedruna.palcole.domain.model.FavoritePromotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface FavoritePromotionStorage {

  PageModel<FavoritePromotion> selectAllFavoritePromotions(PageableModel pageableModel);

  FavoritePromotion findFavoritePromotionById(Integer userId, Integer promotionId);

  FavoritePromotion insertFavoritePromotion(FavoritePromotion newFavoritePromotion);

  void deleteFavoritePromotion(Integer userId, Integer promotionId);
}
