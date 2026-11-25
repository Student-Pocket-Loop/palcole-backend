package org.vedruna.palcole.application.categories;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.exception.categories.CategoryNotFoundException;
import org.vedruna.palcole.domain.ports.outbound.CategoryStorage;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryUseCaseImplTest {

  @Mock private CategoryStorage categoryStorage;

  @InjectMocks private DeleteCategoryUseCaseImpl deleteCategoryUseCaseImpl;

  @Test
  @DisplayName("Test caso de uso deleteCategory Ok")
  void deleteCategoryOkTest() {

    // INPUT
    Integer categoryIdInput = 1;

    // MOCKS
    doNothing().when(categoryStorage).deleteCategory(categoryIdInput);

    // EJECUCIÓN Y VALIDACIONES
    assertDoesNotThrow(() -> deleteCategoryUseCaseImpl.deleteCategory(categoryIdInput));
    verify(categoryStorage).deleteCategory(categoryIdInput);
  }

  @Test
  @DisplayName("Test caso de uso deleteCategory Ko")
  void deleteCategoryKoTest() {

    // INPUT
    Integer categoryIdInput = 99;

    // MOCKS
    doThrow(new CategoryNotFoundException(99))
        .when(categoryStorage)
        .deleteCategory(categoryIdInput);

    // EJECUCIÓN Y VALIDACIONES
    CategoryNotFoundException ex =
        assertThrows(
            CategoryNotFoundException.class,
            () -> deleteCategoryUseCaseImpl.deleteCategory(categoryIdInput));

    assertEquals("No se encuentra la categoría con id: 99", ex.getMessage());
  }
}
