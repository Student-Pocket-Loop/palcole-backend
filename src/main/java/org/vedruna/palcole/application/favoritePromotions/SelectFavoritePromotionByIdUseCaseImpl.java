package org.vedruna.palcole.application.favoritePromotions;

import org.vedruna.palcole.domain.model.FavoritePromotion;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.SelectFavoritePromotionByIdUseCase;
import org.vedruna.palcole.domain.ports.outbound.FavoritePromotionStorage;

public class SelectFavoritePromotionByIdUseCaseImpl implements SelectFavoritePromotionByIdUseCase {

  private final FavoritePromotionStorage favoritePromotionStorage;

  public SelectFavoritePromotionByIdUseCaseImpl(FavoritePromotionStorage favoritePromotionStorage) {
    this.favoritePromotionStorage = favoritePromotionStorage;
  }

  @Override
  public FavoritePromotion selectFavoritePromotionById(Integer userId, Integer promotionId) {
    return favoritePromotionStorage.findFavoritePromotionById(userId, promotionId);
  }
}
