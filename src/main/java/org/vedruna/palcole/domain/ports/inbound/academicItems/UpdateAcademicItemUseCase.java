package org.vedruna.palcole.domain.ports.inbound.academicItems;

import org.vedruna.palcole.domain.model.AcademicItem;

public interface UpdateAcademicItemUseCase {
  AcademicItem updateAcademicItem(Integer itemId, AcademicItem newAcademicItem);
}
