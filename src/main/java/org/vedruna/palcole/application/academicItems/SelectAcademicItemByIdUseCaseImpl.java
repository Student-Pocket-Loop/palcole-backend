package org.vedruna.palcole.application.academicItems;

import org.vedruna.palcole.domain.model.AcademicItem;
import org.vedruna.palcole.domain.ports.inbound.academicItems.SelectAcademicItemByIdUseCase;
import org.vedruna.palcole.domain.ports.outbound.AcademicItemStorage;

public class SelectAcademicItemByIdUseCaseImpl implements SelectAcademicItemByIdUseCase {

  private final AcademicItemStorage academicItemStorage;

  public SelectAcademicItemByIdUseCaseImpl(AcademicItemStorage academicItemStorage) {
    this.academicItemStorage = academicItemStorage;
  }

  @Override
  public AcademicItem selectAcademicItemById(Integer itemId) {
    return academicItemStorage.findAcademicItemById(itemId);
  }
}
