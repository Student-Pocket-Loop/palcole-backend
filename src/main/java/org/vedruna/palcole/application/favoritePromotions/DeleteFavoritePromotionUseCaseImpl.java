package org.vedruna.palcole.application.favoritePromotions;

import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.DeleteFavoritePromotionUseCase;
import org.vedruna.palcole.domain.ports.outbound.FavoritePromotionStorage;

public class DeleteFavoritePromotionUseCaseImpl implements DeleteFavoritePromotionUseCase {

  private final FavoritePromotionStorage favoritePromotionStorage;

  public DeleteFavoritePromotionUseCaseImpl(FavoritePromotionStorage favoritePromotionStorage) {
    this.favoritePromotionStorage = favoritePromotionStorage;
  }

  @Override
  public void deleteFavoritePromotion(Integer userId, Integer promotionId) {
    favoritePromotionStorage.deleteFavoritePromotion(userId, promotionId);
  }
}
