package org.vedruna.palcole.domain.ports.inbound.promotions;

import org.vedruna.palcole.domain.model.Promotion;

public interface NewPromotionUseCase {
  Promotion newPromotion(Promotion newPromotion);
}
