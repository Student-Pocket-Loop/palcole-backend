package org.vedruna.palcole.domain.ports.inbound.categories;

import org.vedruna.palcole.domain.model.Category;

public interface SelectCategoryByIdUseCase {
  Category selectCategoryById(Integer categoryId);
}
