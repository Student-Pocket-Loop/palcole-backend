package org.vedruna.palcole.application.categories;

import org.vedruna.palcole.domain.model.Category;
import org.vedruna.palcole.domain.ports.inbound.categories.NewCategoryUseCase;
import org.vedruna.palcole.domain.ports.outbound.CategoryStorage;

public class NewCategoryUseCaseImpl implements NewCategoryUseCase {

  private final CategoryStorage categoryStorage;

  public NewCategoryUseCaseImpl(CategoryStorage categoryStorage) {
    this.categoryStorage = categoryStorage;
  }

  @Override
  public Category newCategory(Category newCategory) {
    return categoryStorage.insertCategory(newCategory);
  }
}
