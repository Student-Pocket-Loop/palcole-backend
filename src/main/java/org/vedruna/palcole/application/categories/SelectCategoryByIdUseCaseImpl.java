package org.vedruna.palcole.application.categories;

import org.vedruna.palcole.domain.model.Category;
import org.vedruna.palcole.domain.ports.inbound.categories.SelectCategoryByIdUseCase;
import org.vedruna.palcole.domain.ports.outbound.CategoryStorage;

public class SelectCategoryByIdUseCaseImpl implements SelectCategoryByIdUseCase {

  private final CategoryStorage categoryStorage;

  public SelectCategoryByIdUseCaseImpl(CategoryStorage categoryStorage) {
    this.categoryStorage = categoryStorage;
  }

  @Override
  public Category selectCategoryById(Integer categoryId) {
    return categoryStorage.findCategoryById(categoryId);
  }
}
