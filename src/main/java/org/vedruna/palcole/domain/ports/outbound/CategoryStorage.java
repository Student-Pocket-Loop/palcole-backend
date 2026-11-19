package org.vedruna.palcole.domain.ports.outbound;

import org.vedruna.palcole.domain.model.Category;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface CategoryStorage {

  PageModel<Category> selectAllCategories(PageableModel pageableModel);

  Category findCategoryById(Integer categoryId);

  Category updateCategory(Integer categoryId, Category newCategory);

  Category insertCategory(Category newCategory);

  void deleteCategory(Integer categoryId);
}
