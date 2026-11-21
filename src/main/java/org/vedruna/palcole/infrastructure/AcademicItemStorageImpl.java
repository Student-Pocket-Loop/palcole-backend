package org.vedruna.palcole.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.vedruna.palcole.domain.exception.academicItems.AcademicItemNotFoundException;
import org.vedruna.palcole.domain.model.AcademicItem;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.AcademicItemStorage;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.AcademicItemOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.AcademicItemEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.AcademicItemRepository;

@Component
@AllArgsConstructor
public class AcademicItemStorageImpl implements AcademicItemStorage {

  private final AcademicItemRepository academicItemRepository;
  private final AcademicItemOutConverter academicItemOutConverter;

  @Override
  public PageModel<AcademicItem> selectAllAcademicItems(PageableModel pageableModel) {
    Pageable pageable = academicItemOutConverter.toPageable(pageableModel);
    Page<AcademicItemEntity> academicItemPage = academicItemRepository.findAll(pageable);
    return academicItemOutConverter.toAcademicItemPageModel(academicItemPage);
  }

  @Override
  public AcademicItem findAcademicItemById(Integer itemId) {
    AcademicItemEntity academicItemEntity =
        academicItemRepository
            .findById(itemId)
            .orElseThrow(() -> new AcademicItemNotFoundException(itemId));
    return academicItemOutConverter.toDomain(academicItemEntity);
  }

  @Override
  public AcademicItem updateAcademicItem(Integer itemId, AcademicItem newAcademicItem) {
    AcademicItemEntity academicItemEntity =
        academicItemRepository
            .findById(itemId)
            .orElseThrow(() -> new AcademicItemNotFoundException(itemId));

    academicItemEntity.setCategoryId(newAcademicItem.getCategoryId());
    academicItemEntity.setTitle(newAcademicItem.getTitle());
    academicItemEntity.setDescription(newAcademicItem.getDescription());
    if (newAcademicItem.getPrice() != null) {
      academicItemEntity.setPrice(newAcademicItem.getPrice());
    }
    academicItemEntity.setItemCondition(newAcademicItem.getItemCondition());
    if (newAcademicItem.getListingType() != null) {
      academicItemEntity.setListingType(newAcademicItem.getListingType());
    }
    if (newAcademicItem.getSaleStatus() != null) {
      academicItemEntity.setSaleStatus(newAcademicItem.getSaleStatus());
    }
    academicItemEntity.setImageUrl(newAcademicItem.getImageUrl());

    return academicItemOutConverter.toDomain(academicItemRepository.save(academicItemEntity));
  }

  @Override
  public AcademicItem insertAcademicItem(AcademicItem newAcademicItem) {
    return academicItemOutConverter.toDomain(
        academicItemRepository.save(academicItemOutConverter.toEntity(newAcademicItem)));
  }

  @Override
  public void deleteAcademicItem(Integer itemId) {
    if (!academicItemRepository.existsById(itemId)) {
      throw new AcademicItemNotFoundException(itemId);
    }
    academicItemRepository.deleteById(itemId);
  }
}
