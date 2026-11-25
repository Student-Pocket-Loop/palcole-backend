package org.vedruna.palcole.application.categories;

import org.vedruna.palcole.domain.ports.inbound.categories.DeleteCategoryUseCase;
import org.vedruna.palcole.domain.ports.outbound.CategoryStorage;

public class DeleteCategoryUseCaseImpl implements DeleteCategoryUseCase {

  private final CategoryStorage categoryStorage;

  public DeleteCategoryUseCaseImpl(CategoryStorage categoryStorage) {
    this.categoryStorage = categoryStorage;
  }

  @Override
  public void deleteCategory(Integer categoryId) {
    categoryStorage.deleteCategory(categoryId);
  }
}
