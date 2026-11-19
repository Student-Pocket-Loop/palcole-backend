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
class UpdateCategoryUseCaseImplTest {

  @Mock private CategoryStorage categoryStorage;

  @InjectMocks private UpdateCategoryUseCaseImpl updateCategoryUseCaseImpl;

  private Category crearCategory(Integer categoryId, String name) {
    Category o = new Category();
    o.setCategoryId(categoryId);
    o.setName(name);
    return o;
  }

  @Test
  @DisplayName("Test caso de uso updateCategory Ok")
  void updateCategoryOkTest() {

    // INPUT
    Integer categoryIdInput = 1;
    Category newCategoryInput = crearCategory(null, "Material escolar");

    // ESPERADO
    Category categoryEsperado = crearCategory(1, "Material escolar");

    // MOCKS
    Category categoryActualizadoMock = crearCategory(1, "Material escolar");

    when(categoryStorage.updateCategory(categoryIdInput, newCategoryInput))
        .thenReturn(categoryActualizadoMock);

    // EJECUCIÓN
    Category res = updateCategoryUseCaseImpl.updateCategory(categoryIdInput, newCategoryInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(categoryEsperado, res);
        });
    verify(categoryStorage).updateCategory(categoryIdInput, newCategoryInput);
  }

  @Test
  @DisplayName("Test caso de uso updateCategory Ko")
  void updateCategoryKoTest() {

    // INPUT
    Integer categoryIdInput = 99;
    Category newCategoryInput = crearCategory(null, "Material escolar");

    // MOCKS
    when(categoryStorage.updateCategory(categoryIdInput, newCategoryInput))
        .thenThrow(new CategoryNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    CategoryNotFoundException ex =
        assertThrows(
            CategoryNotFoundException.class,
            () -> updateCategoryUseCaseImpl.updateCategory(categoryIdInput, newCategoryInput));

    assertEquals("No se encuentra la categoría con id: 99", ex.getMessage());
    verify(categoryStorage).updateCategory(categoryIdInput, newCategoryInput);
  }
}
