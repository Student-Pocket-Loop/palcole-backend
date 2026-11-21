package org.vedruna.palcole.domain.ports.inbound.favoritePromotions;

import org.vedruna.palcole.domain.model.FavoritePromotion;

public interface NewFavoritePromotionUseCase {
  FavoritePromotion newFavoritePromotion(FavoritePromotion newFavoritePromotion);
}
