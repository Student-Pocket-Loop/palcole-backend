package org.vedruna.palcole.application.promotions;

import org.vedruna.palcole.domain.model.Promotion;
import org.vedruna.palcole.domain.ports.inbound.promotions.UpdatePromotionUseCase;
import org.vedruna.palcole.domain.ports.outbound.PromotionStorage;

public class UpdatePromotionUseCaseImpl implements UpdatePromotionUseCase {

  private final PromotionStorage promotionStorage;

  public UpdatePromotionUseCaseImpl(PromotionStorage promotionStorage) {
    this.promotionStorage = promotionStorage;
  }

  @Override
  public Promotion updatePromotion(Integer promotionId, Promotion newPromotion) {
    PromotionRules.validate(newPromotion);
    return promotionStorage.updatePromotion(promotionId, newPromotion);
  }
}
