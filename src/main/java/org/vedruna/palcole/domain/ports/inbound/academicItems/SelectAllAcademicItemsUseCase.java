package org.vedruna.palcole.domain.ports.inbound.academicItems;

import org.vedruna.palcole.domain.model.AcademicItem;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface SelectAllAcademicItemsUseCase {
  PageModel<AcademicItem> selectAllAcademicItems(PageableModel pageableModel);
}
