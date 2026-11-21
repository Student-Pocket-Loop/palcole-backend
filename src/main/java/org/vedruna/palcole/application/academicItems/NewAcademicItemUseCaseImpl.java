package org.vedruna.palcole.application.academicItems;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.vedruna.palcole.domain.model.AcademicItem;
import org.vedruna.palcole.domain.model.enums.ListingType;
import org.vedruna.palcole.domain.model.enums.SaleStatus;
import org.vedruna.palcole.domain.ports.inbound.academicItems.NewAcademicItemUseCase;
import org.vedruna.palcole.domain.ports.outbound.AcademicItemStorage;

public class NewAcademicItemUseCaseImpl implements NewAcademicItemUseCase {

  private final AcademicItemStorage academicItemStorage;

  public NewAcademicItemUseCaseImpl(AcademicItemStorage academicItemStorage) {
    this.academicItemStorage = academicItemStorage;
  }

  @Override
  public AcademicItem newAcademicItem(AcademicItem newAcademicItem) {
    if (newAcademicItem.getPrice() == null) {
      newAcademicItem.setPrice(BigDecimal.ZERO);
    }
    if (newAcademicItem.getListingType() == null) {
      newAcademicItem.setListingType(ListingType.VENTA);
    }
    if (newAcademicItem.getSaleStatus() == null) {
      newAcademicItem.setSaleStatus(SaleStatus.DISPONIBLE);
    }
    newAcademicItem.setPublishedAt(LocalDateTime.now());
    return academicItemStorage.insertAcademicItem(newAcademicItem);
  }
}
