package org.vedruna.palcole.application.categories;

import org.vedruna.palcole.domain.model.Category;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.categories.SelectAllCategoriesUseCase;
import org.vedruna.palcole.domain.ports.outbound.CategoryStorage;

public class SelectAllCategoriesUseCaseImpl implements SelectAllCategoriesUseCase {

  private final CategoryStorage categoryStorage;

  public SelectAllCategoriesUseCaseImpl(CategoryStorage categoryStorage) {
    this.categoryStorage = categoryStorage;
  }

  @Override
  public PageModel<Category> selectAllCategories(PageableModel pageableModel) {
    return categoryStorage.selectAllCategories(pageableModel);
  }
}
