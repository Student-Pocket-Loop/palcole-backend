package org.vedruna.palcole.application.categories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.model.Category;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.CategoryStorage;

@ExtendWith(MockitoExtension.class)
class SelectAllCategoriesUseCaseImplTest {

  @Mock private CategoryStorage categoryStorage;

  @InjectMocks private SelectAllCategoriesUseCaseImpl selectAllCategoriesUseCaseImpl;

  private Category crearCategory(Integer categoryId, String name) {
    Category o = new Category();
    o.setCategoryId(categoryId);
    o.setName(name);
    return o;
  }

  @Test
  @DisplayName("Test caso de uso selectAllCategories Ok")
  void selectAllCategoriesOkTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<Category> pageEsperado =
        new PageModel<>(
            List.of(crearCategory(1, "Libros"), crearCategory(2, "Apuntes")), 2, 1, 2, 10, 0);

    // MOCKS
    when(categoryStorage.selectAllCategories(pageableInput)).thenReturn(pageEsperado);

    // EJECUCIÓN
    PageModel<Category> res = selectAllCategoriesUseCaseImpl.selectAllCategories(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(2, res.getTotalElements());
          assertEquals(pageEsperado, res);
        });
    verify(categoryStorage).selectAllCategories(pageableInput);
  }

  @Test
  @DisplayName("Test caso de uso selectAllCategories Ko")
  void selectAllCategoriesKoTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "inexistente: ASC");

    // MOCKS
    when(categoryStorage.selectAllCategories(pageableInput))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> selectAllCategoriesUseCaseImpl.selectAllCategories(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
  }
}
