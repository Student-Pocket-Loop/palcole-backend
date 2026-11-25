package org.vedruna.palcole.domain.ports.inbound.categories;

import org.vedruna.palcole.domain.model.Category;

public interface NewCategoryUseCase {
  Category newCategory(Category newCategory);
}
