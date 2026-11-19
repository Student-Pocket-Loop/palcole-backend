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
import org.springframework.dao.DataIntegrityViolationException;
import org.vedruna.palcole.domain.model.Category;
import org.vedruna.palcole.domain.ports.outbound.CategoryStorage;

@ExtendWith(MockitoExtension.class)
class NewCategoryUseCaseImplTest {

  @Mock private CategoryStorage categoryStorage;

  @InjectMocks private NewCategoryUseCaseImpl newCategoryUseCaseImpl;

  private Category crearCategory(Integer categoryId, String name) {
    Category o = new Category();
    o.setCategoryId(categoryId);
    o.setName(name);
    return o;
  }

  @Test
  @DisplayName("Test caso de uso newCategory Ok")
  void newCategoryOkTest() {

    // INPUT
    Category newCategoryInput = crearCategory(null, "Libros");

    // ESPERADO
    Category categoryEsperado = crearCategory(3, "Libros");

    // MOCKS
    Category categoryCreadoMock = crearCategory(3, "Libros");

    when(categoryStorage.insertCategory(newCategoryInput)).thenReturn(categoryCreadoMock);

    // EJECUCIÓN
    Category res = newCategoryUseCaseImpl.newCategory(newCategoryInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(categoryEsperado, res);
        });
    verify(categoryStorage).insertCategory(newCategoryInput);
  }

  @Test
  @DisplayName("Test caso de uso newCategory Ko")
  void newCategoryKoTest() {

    // INPUT
    Category newCategoryInput = crearCategory(null, "Libros");

    // MOCKS
    when(categoryStorage.insertCategory(newCategoryInput))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> newCategoryUseCaseImpl.newCategory(newCategoryInput));

    assertNotNull(ex.getMessage());
    verify(categoryStorage).insertCategory(newCategoryInput);
  }
}
