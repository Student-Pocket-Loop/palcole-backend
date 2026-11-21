package org.vedruna.palcole.domain.ports.outbound;

import org.vedruna.palcole.domain.model.AcademicItem;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface AcademicItemStorage {

  PageModel<AcademicItem> selectAllAcademicItems(PageableModel pageableModel);

  AcademicItem findAcademicItemById(Integer itemId);

  AcademicItem updateAcademicItem(Integer itemId, AcademicItem newAcademicItem);

  AcademicItem insertAcademicItem(AcademicItem newAcademicItem);

  void deleteAcademicItem(Integer itemId);
}
