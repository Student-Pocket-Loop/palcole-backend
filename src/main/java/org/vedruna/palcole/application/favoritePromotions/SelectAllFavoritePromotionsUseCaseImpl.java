package org.vedruna.palcole.application.favoritePromotions;

import org.vedruna.palcole.domain.model.FavoritePromotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.SelectAllFavoritePromotionsUseCase;
import org.vedruna.palcole.domain.ports.outbound.FavoritePromotionStorage;

public class SelectAllFavoritePromotionsUseCaseImpl implements SelectAllFavoritePromotionsUseCase {

  private final FavoritePromotionStorage favoritePromotionStorage;

  public SelectAllFavoritePromotionsUseCaseImpl(FavoritePromotionStorage favoritePromotionStorage) {
    this.favoritePromotionStorage = favoritePromotionStorage;
  }

  @Override
  public PageModel<FavoritePromotion> selectAllFavoritePromotions(PageableModel pageableModel) {
    return favoritePromotionStorage.selectAllFavoritePromotions(pageableModel);
  }
}
