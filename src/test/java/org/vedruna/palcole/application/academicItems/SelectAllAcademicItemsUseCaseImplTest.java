package org.vedruna.palcole.application.academicItems;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.model.AcademicItem;
import org.vedruna.palcole.domain.model.enums.ItemCondition;
import org.vedruna.palcole.domain.model.enums.ListingType;
import org.vedruna.palcole.domain.model.enums.SaleStatus;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.AcademicItemStorage;

@ExtendWith(MockitoExtension.class)
class SelectAllAcademicItemsUseCaseImplTest {

  @Mock private AcademicItemStorage academicItemStorage;

  @InjectMocks private SelectAllAcademicItemsUseCaseImpl selectAllAcademicItemsUseCaseImpl;

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
  @DisplayName("Test caso de uso selectAllAcademicItems Ok")
  void selectAllAcademicItemsOkTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<AcademicItem> pageEsperado =
        new PageModel<>(
            List.of(
                crearAcademicItem(1, "Calculadora"), crearAcademicItem(2, "Libro de Matemáticas")),
            2,
            1,
            2,
            10,
            0);

    // MOCKS
    when(academicItemStorage.selectAllAcademicItems(pageableInput)).thenReturn(pageEsperado);

    // EJECUCIÓN
    PageModel<AcademicItem> res =
        selectAllAcademicItemsUseCaseImpl.selectAllAcademicItems(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(2, res.getTotalElements());
          assertEquals(pageEsperado, res);
        });
    verify(academicItemStorage).selectAllAcademicItems(pageableInput);
  }

  @Test
  @DisplayName("Test caso de uso selectAllAcademicItems Ko")
  void selectAllAcademicItemsKoTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "inexistente: ASC");

    // MOCKS
    when(academicItemStorage.selectAllAcademicItems(pageableInput))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> selectAllAcademicItemsUseCaseImpl.selectAllAcademicItems(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
  }
}
