package org.vedruna.palcole.domain.ports.inbound.favoritePromotions;

import org.vedruna.palcole.domain.model.FavoritePromotion;

public interface SelectFavoritePromotionByIdUseCase {
  FavoritePromotion selectFavoritePromotionById(Integer userId, Integer promotionId);
}
