package org.vedruna.palcole.domain.ports.inbound.categories;

import org.vedruna.palcole.domain.model.Category;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface SelectAllCategoriesUseCase {
  PageModel<Category> selectAllCategories(PageableModel pageableModel);
}
