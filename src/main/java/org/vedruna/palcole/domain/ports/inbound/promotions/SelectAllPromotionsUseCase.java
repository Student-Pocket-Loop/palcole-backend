package org.vedruna.palcole.domain.ports.inbound.promotions;

import org.vedruna.palcole.domain.model.Promotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface SelectAllPromotionsUseCase {
  PageModel<Promotion> selectAllPromotions(PageableModel pageableModel);
}
