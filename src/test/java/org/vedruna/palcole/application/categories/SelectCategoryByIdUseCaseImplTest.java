package org.vedruna.palcole.application.categories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.exception.categories.CategoryNotFoundException;
import org.vedruna.palcole.domain.model.Category;
import org.vedruna.palcole.domain.ports.outbound.CategoryStorage;

@ExtendWith(MockitoExtension.class)
class SelectCategoryByIdUseCaseImplTest {

  @Mock private CategoryStorage categoryStorage;

  @InjectMocks private SelectCategoryByIdUseCaseImpl selectCategoryByIdUseCaseImpl;

  private Category crearCategory(Integer categoryId, String name) {
    Category o = new Category();
    o.setCategoryId(categoryId);
    o.setName(name);
    return o;
  }

  @Test
  @DisplayName("Test caso de uso selectCategoryById Ok")
  void selectCategoryByIdOkTest() {

    // INPUT
    Integer categoryIdInput = 1;

    // ESPERADO
    Category categoryEsperado = crearCategory(1, "Libros");

    // MOCKS
    when(categoryStorage.findCategoryById(categoryIdInput)).thenReturn(categoryEsperado);

    // EJECUCIÓN
    Category res = selectCategoryByIdUseCaseImpl.selectCategoryById(categoryIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(categoryEsperado, res);
        });
    verify(categoryStorage).findCategoryById(categoryIdInput);
  }

  @Test
  @DisplayName("Test caso de uso selectCategoryById Ko")
  void selectCategoryByIdKoTest() {

    // INPUT
    Integer categoryIdInput = 99;

    // MOCKS
    when(categoryStorage.findCategoryById(categoryIdInput))
        .thenThrow(new CategoryNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    CategoryNotFoundException ex =
        assertThrows(
            CategoryNotFoundException.class,
            () -> selectCategoryByIdUseCaseImpl.selectCategoryById(categoryIdInput));

    assertEquals("No se encuentra la categoría con id: 99", ex.getMessage());
  }
}
