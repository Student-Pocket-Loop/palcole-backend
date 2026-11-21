package org.vedruna.palcole.application.promotions;

import org.vedruna.palcole.domain.model.Promotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.promotions.SelectAllPromotionsUseCase;
import org.vedruna.palcole.domain.ports.outbound.PromotionStorage;

public class SelectAllPromotionsUseCaseImpl implements SelectAllPromotionsUseCase {

  private final PromotionStorage promotionStorage;

  public SelectAllPromotionsUseCaseImpl(PromotionStorage promotionStorage) {
    this.promotionStorage = promotionStorage;
  }

  @Override
  public PageModel<Promotion> selectAllPromotions(PageableModel pageableModel) {
    return promotionStorage.selectAllPromotions(pageableModel);
  }
}
