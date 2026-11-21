package org.vedruna.palcole.application.promotions;

import org.vedruna.palcole.domain.model.Promotion;
import org.vedruna.palcole.domain.ports.inbound.promotions.SelectPromotionByIdUseCase;
import org.vedruna.palcole.domain.ports.outbound.PromotionStorage;

public class SelectPromotionByIdUseCaseImpl implements SelectPromotionByIdUseCase {

  private final PromotionStorage promotionStorage;

  public SelectPromotionByIdUseCaseImpl(PromotionStorage promotionStorage) {
    this.promotionStorage = promotionStorage;
  }

  @Override
  public Promotion selectPromotionById(Integer promotionId) {
    return promotionStorage.findPromotionById(promotionId);
  }
}
