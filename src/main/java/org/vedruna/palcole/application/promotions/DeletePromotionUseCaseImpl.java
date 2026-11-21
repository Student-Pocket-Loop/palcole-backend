package org.vedruna.palcole.application.promotions;

import org.vedruna.palcole.domain.ports.inbound.promotions.DeletePromotionUseCase;
import org.vedruna.palcole.domain.ports.outbound.PromotionStorage;

public class DeletePromotionUseCaseImpl implements DeletePromotionUseCase {

  private final PromotionStorage promotionStorage;

  public DeletePromotionUseCaseImpl(PromotionStorage promotionStorage) {
    this.promotionStorage = promotionStorage;
  }

  @Override
  public void deletePromotion(Integer promotionId) {
    promotionStorage.deletePromotion(promotionId);
  }
}
