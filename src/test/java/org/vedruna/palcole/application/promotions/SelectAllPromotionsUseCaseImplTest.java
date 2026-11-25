package org.vedruna.palcole.application.promotions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.model.Promotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.PromotionStorage;

@ExtendWith(MockitoExtension.class)
class SelectAllPromotionsUseCaseImplTest {

  @Mock private PromotionStorage promotionStorage;

  @InjectMocks private SelectAllPromotionsUseCaseImpl selectAllPromotionsUseCaseImpl;

  private Promotion crearPromotion(Integer promotionId, String title) {
    Promotion o = new Promotion();
    o.setPromotionId(promotionId);
    o.setCategoryId(1);
    o.setTitle(title);
    o.setDescription("Descuento por la vuelta al cole");
    o.setOriginalPrice(new BigDecimal("20.00"));
    o.setDiscountPrice(new BigDecimal("15.00"));
    o.setDiscountPercentage(25);
    o.setPromoCode("VUELTA25");
    o.setStartDate(LocalDate.of(2026, 9, 1));
    o.setEndDate(LocalDate.of(2026, 9, 30));
    return o;
  }

  @Test
  @DisplayName("Test caso de uso selectAllPromotions Ok")
  void selectAllPromotionsOkTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<Promotion> pageEsperado =
        new PageModel<>(
            List.of(crearPromotion(1, "Papelería"), crearPromotion(2, "Mochilas")), 2, 1, 2, 10, 0);

    // MOCKS
    when(promotionStorage.selectAllPromotions(pageableInput)).thenReturn(pageEsperado);

    // EJECUCIÓN
    PageModel<Promotion> res = selectAllPromotionsUseCaseImpl.selectAllPromotions(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(2, res.getTotalElements());
          assertEquals(pageEsperado, res);
        });
    verify(promotionStorage).selectAllPromotions(pageableInput);
  }

  @Test
  @DisplayName("Test caso de uso selectAllPromotions Ko")
  void selectAllPromotionsKoTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "inexistente: ASC");

    // MOCKS
    when(promotionStorage.selectAllPromotions(pageableInput))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> selectAllPromotionsUseCaseImpl.selectAllPromotions(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
  }
}
