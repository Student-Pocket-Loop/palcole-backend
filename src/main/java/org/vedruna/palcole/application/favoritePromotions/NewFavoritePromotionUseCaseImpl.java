package org.vedruna.palcole.application.favoritePromotions;

import org.vedruna.palcole.domain.model.FavoritePromotion;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.NewFavoritePromotionUseCase;
import org.vedruna.palcole.domain.ports.outbound.FavoritePromotionStorage;

public class NewFavoritePromotionUseCaseImpl implements NewFavoritePromotionUseCase {

  private final FavoritePromotionStorage favoritePromotionStorage;

  public NewFavoritePromotionUseCaseImpl(FavoritePromotionStorage favoritePromotionStorage) {
    this.favoritePromotionStorage = favoritePromotionStorage;
  }

  @Override
  public FavoritePromotion newFavoritePromotion(FavoritePromotion newFavoritePromotion) {
    return favoritePromotionStorage.insertFavoritePromotion(newFavoritePromotion);
  }
}
