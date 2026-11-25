package org.vedruna.palcole.application.academicItems;

import org.vedruna.palcole.domain.model.AcademicItem;
import org.vedruna.palcole.domain.ports.inbound.academicItems.UpdateAcademicItemUseCase;
import org.vedruna.palcole.domain.ports.outbound.AcademicItemStorage;

public class UpdateAcademicItemUseCaseImpl implements UpdateAcademicItemUseCase {

  private final AcademicItemStorage academicItemStorage;

  public UpdateAcademicItemUseCaseImpl(AcademicItemStorage academicItemStorage) {
    this.academicItemStorage = academicItemStorage;
  }

  @Override
  public AcademicItem updateAcademicItem(Integer itemId, AcademicItem newAcademicItem) {
    return academicItemStorage.updateAcademicItem(itemId, newAcademicItem);
  }
}
