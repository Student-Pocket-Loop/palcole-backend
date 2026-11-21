package org.vedruna.palcole.application.academicItems;

import org.vedruna.palcole.domain.model.AcademicItem;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.academicItems.SelectAllAcademicItemsUseCase;
import org.vedruna.palcole.domain.ports.outbound.AcademicItemStorage;

public class SelectAllAcademicItemsUseCaseImpl implements SelectAllAcademicItemsUseCase {

  private final AcademicItemStorage academicItemStorage;

  public SelectAllAcademicItemsUseCaseImpl(AcademicItemStorage academicItemStorage) {
    this.academicItemStorage = academicItemStorage;
  }

  @Override
  public PageModel<AcademicItem> selectAllAcademicItems(PageableModel pageableModel) {
    return academicItemStorage.selectAllAcademicItems(pageableModel);
  }
}
