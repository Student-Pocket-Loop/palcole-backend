package org.vedruna.palcole.domain.ports.inbound.favoritePromotions;

public interface DeleteFavoritePromotionUseCase {
  void deleteFavoritePromotion(Integer userId, Integer promotionId);
}
