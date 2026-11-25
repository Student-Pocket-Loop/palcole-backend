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
import org.springframework.dao.DataIntegrityViolationException;
import org.vedruna.palcole.domain.model.AcademicItem;
import org.vedruna.palcole.domain.model.enums.ItemCondition;
import org.vedruna.palcole.domain.model.enums.ListingType;
import org.vedruna.palcole.domain.model.enums.SaleStatus;
import org.vedruna.palcole.domain.ports.outbound.AcademicItemStorage;

@ExtendWith(MockitoExtension.class)
class NewAcademicItemUseCaseImplTest {

  @Mock private AcademicItemStorage academicItemStorage;

  @InjectMocks private NewAcademicItemUseCaseImpl newAcademicItemUseCaseImpl;

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
  @DisplayName("Test caso de uso newAcademicItem Ok")
  void newAcademicItemOkTest() {

    // INPUT
    AcademicItem newAcademicItemInput = crearAcademicItem(null, "Calculadora");

    // ESPERADO
    AcademicItem academicItemEsperado = crearAcademicItem(3, "Calculadora");

    // MOCKS
    AcademicItem academicItemCreadoMock = crearAcademicItem(3, "Calculadora");

    when(academicItemStorage.insertAcademicItem(newAcademicItemInput))
        .thenReturn(academicItemCreadoMock);

    // EJECUCIÓN
    AcademicItem res = newAcademicItemUseCaseImpl.newAcademicItem(newAcademicItemInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(academicItemEsperado, res);
          assertNotNull(res.getPublishedAt());
        });
    verify(academicItemStorage).insertAcademicItem(newAcademicItemInput);
  }

  @Test
  @DisplayName("Test caso de uso newAcademicItem Ko")
  void newAcademicItemKoTest() {

    // INPUT
    AcademicItem newAcademicItemInput = crearAcademicItem(null, "Calculadora");

    // MOCKS
    when(academicItemStorage.insertAcademicItem(newAcademicItemInput))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> newAcademicItemUseCaseImpl.newAcademicItem(newAcademicItemInput));

    assertNotNull(ex.getMessage());
    verify(academicItemStorage).insertAcademicItem(newAcademicItemInput);
  }

  @Test
  @DisplayName("Test caso de uso newAcademicItem aplica valores por defecto")
  void newAcademicItemDefaultsTest() {

    // INPUT
    AcademicItem newAcademicItemInput = crearAcademicItem(null, "Calculadora");
    newAcademicItemInput.setPrice(null);
    newAcademicItemInput.setListingType(null);
    newAcademicItemInput.setSaleStatus(null);
    newAcademicItemInput.setPublishedAt(null);

    // MOCKS
    when(academicItemStorage.insertAcademicItem(newAcademicItemInput))
        .thenReturn(newAcademicItemInput);

    // EJECUCIÓN
    AcademicItem res = newAcademicItemUseCaseImpl.newAcademicItem(newAcademicItemInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(BigDecimal.ZERO, res.getPrice());
          assertEquals(ListingType.VENTA, res.getListingType());
          assertEquals(SaleStatus.DISPONIBLE, res.getSaleStatus());
          assertNotNull(res.getPublishedAt());
        });
  }
}
