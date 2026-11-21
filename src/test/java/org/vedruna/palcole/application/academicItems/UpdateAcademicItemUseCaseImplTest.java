package org.vedruna.palcole.application.academicItems;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.exception.academicItems.AcademicItemNotFoundException;
import org.vedruna.palcole.domain.model.AcademicItem;
import org.vedruna.palcole.domain.model.enums.ItemCondition;
import org.vedruna.palcole.domain.model.enums.ListingType;
import org.vedruna.palcole.domain.model.enums.SaleStatus;
import org.vedruna.palcole.domain.ports.outbound.AcademicItemStorage;

@ExtendWith(MockitoExtension.class)
class UpdateAcademicItemUseCaseImplTest {

  @Mock private AcademicItemStorage academicItemStorage;

  @InjectMocks private UpdateAcademicItemUseCaseImpl updateAcademicItemUseCaseImpl;

  private AcademicItem crearAcademicItem(Integer itemId, String title) {
    AcademicItem o = new AcademicItem();
    o.setItemId(itemId);
    o.setStudentId(1);
    o.setCategoryId(1);
    o.setTitle(title);
    o.setDescription("En buen estado");
    o.setPrice(new BigDecimal("10.00"));
    o.setItemCondition(ItemCondition.USADO);
    o.setListingType(ListingType.VENTA);
    o.setSaleStatus(SaleStatus.DISPONIBLE);
    o.setPublishedAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    o.setImageUrl("https://ejemplo.com/calculadora.jpg");
    return o;
  }

  @Test
  @DisplayName("Test caso de uso updateAcademicItem Ok")
  void updateAcademicItemOkTest() {

    // INPUT
    Integer itemIdInput = 1;
    AcademicItem newAcademicItemInput = crearAcademicItem(null, "Calculadora actualizada");

    // ESPERADO
    AcademicItem academicItemEsperado = crearAcademicItem(1, "Calculadora actualizada");

    // MOCKS
    AcademicItem academicItemActualizadoMock = crearAcademicItem(1, "Calculadora actualizada");

    when(academicItemStorage.updateAcademicItem(itemIdInput, newAcademicItemInput))
        .thenReturn(academicItemActualizadoMock);

    // EJECUCIÓN
    AcademicItem res =
        updateAcademicItemUseCaseImpl.updateAcademicItem(itemIdInput, newAcademicItemInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(academicItemEsperado, res);
        });
    verify(academicItemStorage).updateAcademicItem(itemIdInput, newAcademicItemInput);
  }

  @Test
  @DisplayName("Test caso de uso updateAcademicItem Ko")
  void updateAcademicItemKoTest() {

    // INPUT
    Integer itemIdInput = 99;
    AcademicItem newAcademicItemInput = crearAcademicItem(null, "Calculadora actualizada");

    // MOCKS
    when(academicItemStorage.updateAcademicItem(itemIdInput, newAcademicItemInput))
        .thenThrow(new AcademicItemNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    AcademicItemNotFoundException ex =
        assertThrows(
            AcademicItemNotFoundException.class,
            () ->
                updateAcademicItemUseCaseImpl.updateAcademicItem(
                    itemIdInput, newAcademicItemInput));

    assertEquals("No se encuentra el artículo con id: 99", ex.getMessage());
    verify(academicItemStorage).updateAcademicItem(itemIdInput, newAcademicItemInput);
  }
}
