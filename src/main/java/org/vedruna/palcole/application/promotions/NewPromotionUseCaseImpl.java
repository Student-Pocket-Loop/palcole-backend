package org.vedruna.palcole.application.promotions;

import org.vedruna.palcole.domain.model.Promotion;
import org.vedruna.palcole.domain.ports.inbound.promotions.NewPromotionUseCase;
import org.vedruna.palcole.domain.ports.outbound.PromotionStorage;

public class NewPromotionUseCaseImpl implements NewPromotionUseCase {

  private final PromotionStorage promotionStorage;

  public NewPromotionUseCaseImpl(PromotionStorage promotionStorage) {
    this.promotionStorage = promotionStorage;
  }

  @Override
  public Promotion newPromotion(Promotion newPromotion) {
    PromotionRules.validate(newPromotion);
    return promotionStorage.insertPromotion(newPromotion);
  }
}
