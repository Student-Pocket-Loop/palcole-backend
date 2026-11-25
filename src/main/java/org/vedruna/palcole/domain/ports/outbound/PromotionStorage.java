package org.vedruna.palcole.domain.ports.outbound;

import org.vedruna.palcole.domain.model.Promotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface PromotionStorage {

  PageModel<Promotion> selectAllPromotions(PageableModel pageableModel);

  Promotion findPromotionById(Integer promotionId);

  Promotion updatePromotion(Integer promotionId, Promotion newPromotion);

  Promotion insertPromotion(Promotion newPromotion);

  void deletePromotion(Integer promotionId);
}
