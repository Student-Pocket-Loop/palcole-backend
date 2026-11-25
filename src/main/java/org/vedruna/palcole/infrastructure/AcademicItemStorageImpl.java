package org.vedruna.palcole.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.vedruna.palcole.domain.exception.academicItems.AcademicItemNotFoundException;
import org.vedruna.palcole.domain.exception.categories.CategoryNotFoundException;
import org.vedruna.palcole.domain.model.AcademicItem;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.AcademicItemStorage;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.AcademicItemOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.AcademicItemEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.CategoryEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.AcademicItemRepository;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.CategoryRepository;
import org.vedruna.palcole.persistance.exception.UserNotFoundException;
import org.vedruna.palcole.persistance.model.UserEntity;
import org.vedruna.palcole.persistance.repository.UserRepository;

@Component
@AllArgsConstructor
public class AcademicItemStorageImpl implements AcademicItemStorage {

  private final AcademicItemRepository academicItemRepository;
  private final AcademicItemOutConverter academicItemOutConverter;
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;

  @Override
  @Cacheable(
      value = "academicItemsPage",
      key = "#pageableModel.page + '-' + #pageableModel.size + '-' + #pageableModel.sort")
  public PageModel<AcademicItem> selectAllAcademicItems(PageableModel pageableModel) {
    Pageable pageable = academicItemOutConverter.toPageable(pageableModel);
    Page<AcademicItemEntity> academicItemPage = academicItemRepository.findAll(pageable);
    return academicItemOutConverter.toAcademicItemPageModel(academicItemPage);
  }

  @Override
  @Cacheable(value = "academicItems", key = "#itemId")
  public AcademicItem findAcademicItemById(Integer itemId) {
    AcademicItemEntity academicItemEntity =
        academicItemRepository
            .findById(itemId)
            .orElseThrow(() -> new AcademicItemNotFoundException(itemId));
    return academicItemOutConverter.toDomain(academicItemEntity);
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(value = "academicItems", key = "#itemId"),
        @CacheEvict(value = "academicItemsPage", allEntries = true)
      })
  public AcademicItem updateAcademicItem(Integer itemId, AcademicItem newAcademicItem) {
    AcademicItemEntity academicItemEntity =
        academicItemRepository
            .findById(itemId)
            .orElseThrow(() -> new AcademicItemNotFoundException(itemId));

    if (newAcademicItem.getCategoryId() != null) {
      academicItemEntity.setCategory(findCategory(newAcademicItem.getCategoryId()));
    }
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
  @CacheEvict(value = "academicItemsPage", allEntries = true)
  public AcademicItem insertAcademicItem(AcademicItem newAcademicItem) {
    AcademicItemEntity academicItemEntity = academicItemOutConverter.toEntity(newAcademicItem);
    academicItemEntity.setStudent(findUser(newAcademicItem.getStudentId()));
    academicItemEntity.setCategory(findCategory(newAcademicItem.getCategoryId()));
    return academicItemOutConverter.toDomain(academicItemRepository.save(academicItemEntity));
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(value = "academicItems", key = "#itemId"),
        @CacheEvict(value = "academicItemsPage", allEntries = true)
      })
  public void deleteAcademicItem(Integer itemId) {
    if (!academicItemRepository.existsById(itemId)) {
      throw new AcademicItemNotFoundException(itemId);
    }
    academicItemRepository.deleteById(itemId);
  }

  private UserEntity findUser(Integer id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  private CategoryEntity findCategory(Integer id) {
    return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
  }
}
