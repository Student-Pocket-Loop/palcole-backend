package org.vedruna.palcole.domain.ports.inbound.promotions;

import org.vedruna.palcole.domain.model.Promotion;

public interface SelectPromotionByIdUseCase {
  Promotion selectPromotionById(Integer promotionId);
}
