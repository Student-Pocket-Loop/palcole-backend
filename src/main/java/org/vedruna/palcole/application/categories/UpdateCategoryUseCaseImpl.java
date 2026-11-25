package org.vedruna.palcole.application.categories;

import org.vedruna.palcole.domain.model.Category;
import org.vedruna.palcole.domain.ports.inbound.categories.UpdateCategoryUseCase;
import org.vedruna.palcole.domain.ports.outbound.CategoryStorage;

public class UpdateCategoryUseCaseImpl implements UpdateCategoryUseCase {

  private final CategoryStorage categoryStorage;

  public UpdateCategoryUseCaseImpl(CategoryStorage categoryStorage) {
    this.categoryStorage = categoryStorage;
  }

  @Override
  public Category updateCategory(Integer categoryId, Category newCategory) {
    return categoryStorage.updateCategory(categoryId, newCategory);
  }
}
