package org.vedruna.palcole.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.vedruna.palcole.domain.exception.categories.CategoryNotFoundException;
import org.vedruna.palcole.domain.model.Category;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.CategoryStorage;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.CategoryOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.CategoryEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.CategoryRepository;

@Component
@AllArgsConstructor
public class CategoryStorageImpl implements CategoryStorage {

  private final CategoryRepository categoryRepository;
  private final CategoryOutConverter categoryOutConverter;

  @Override
  public PageModel<Category> selectAllCategories(PageableModel pageableModel) {
    Pageable pageable = categoryOutConverter.toPageable(pageableModel);
    Page<CategoryEntity> categoryPage = categoryRepository.findAll(pageable);
    return categoryOutConverter.toCategoryPageModel(categoryPage);
  }

  @Override
  public Category findCategoryById(Integer categoryId) {
    CategoryEntity categoryEntity =
        categoryRepository
            .findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    return categoryOutConverter.toDomain(categoryEntity);
  }

  @Override
  public Category updateCategory(Integer categoryId, Category newCategory) {
    CategoryEntity categoryEntity =
        categoryRepository
            .findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));

    categoryEntity.setName(newCategory.getName());

    return categoryOutConverter.toDomain(categoryRepository.save(categoryEntity));
  }

  @Override
  public Category insertCategory(Category newCategory) {
    return categoryOutConverter.toDomain(
        categoryRepository.save(categoryOutConverter.toEntity(newCategory)));
  }

  @Override
  public void deleteCategory(Integer categoryId) {
    if (!categoryRepository.existsById(categoryId)) {
      throw new CategoryNotFoundException(categoryId);
    }
    categoryRepository.deleteById(categoryId);
  }
}
