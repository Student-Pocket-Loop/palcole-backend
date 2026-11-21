package org.vedruna.palcole.application.academicItems;

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
import org.vedruna.palcole.domain.exception.academicItems.AcademicItemNotFoundException;
import org.vedruna.palcole.domain.ports.outbound.AcademicItemStorage;

@ExtendWith(MockitoExtension.class)
class DeleteAcademicItemUseCaseImplTest {

  @Mock private AcademicItemStorage academicItemStorage;

  @InjectMocks private DeleteAcademicItemUseCaseImpl deleteAcademicItemUseCaseImpl;

  @Test
  @DisplayName("Test caso de uso deleteAcademicItem Ok")
  void deleteAcademicItemOkTest() {

    // INPUT
    Integer itemIdInput = 1;

    // MOCKS
    doNothing().when(academicItemStorage).deleteAcademicItem(itemIdInput);

    // EJECUCIÓN Y VALIDACIONES
    assertDoesNotThrow(() -> deleteAcademicItemUseCaseImpl.deleteAcademicItem(itemIdInput));
    verify(academicItemStorage).deleteAcademicItem(itemIdInput);
  }

  @Test
  @DisplayName("Test caso de uso deleteAcademicItem Ko")
  void deleteAcademicItemKoTest() {

    // INPUT
    Integer itemIdInput = 99;

    // MOCKS
    doThrow(new AcademicItemNotFoundException(99))
        .when(academicItemStorage)
        .deleteAcademicItem(itemIdInput);

    // EJECUCIÓN Y VALIDACIONES
    AcademicItemNotFoundException ex =
        assertThrows(
            AcademicItemNotFoundException.class,
            () -> deleteAcademicItemUseCaseImpl.deleteAcademicItem(itemIdInput));

    assertEquals("No se encuentra el artículo con id: 99", ex.getMessage());
  }
}
