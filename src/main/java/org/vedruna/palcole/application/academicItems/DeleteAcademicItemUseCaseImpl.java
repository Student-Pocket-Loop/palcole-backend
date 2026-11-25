package org.vedruna.palcole.application.academicItems;

import org.vedruna.palcole.domain.ports.inbound.academicItems.DeleteAcademicItemUseCase;
import org.vedruna.palcole.domain.ports.outbound.AcademicItemStorage;

public class DeleteAcademicItemUseCaseImpl implements DeleteAcademicItemUseCase {

  private final AcademicItemStorage academicItemStorage;

  public DeleteAcademicItemUseCaseImpl(AcademicItemStorage academicItemStorage) {
    this.academicItemStorage = academicItemStorage;
  }

  @Override
  public void deleteAcademicItem(Integer itemId) {
    academicItemStorage.deleteAcademicItem(itemId);
  }
}
