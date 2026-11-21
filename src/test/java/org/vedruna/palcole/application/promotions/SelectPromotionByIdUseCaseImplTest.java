package org.vedruna.palcole.application.promotions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.exception.promotions.PromotionNotFoundException;
import org.vedruna.palcole.domain.model.Promotion;
import org.vedruna.palcole.domain.ports.outbound.PromotionStorage;

@ExtendWith(MockitoExtension.class)
class SelectPromotionByIdUseCaseImplTest {

  @Mock private PromotionStorage promotionStorage;

  @InjectMocks private SelectPromotionByIdUseCaseImpl selectPromotionByIdUseCaseImpl;

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
  @DisplayName("Test caso de uso selectPromotionById Ok")
  void selectPromotionByIdOkTest() {

    // INPUT
    Integer promotionIdInput = 1;

    // ESPERADO
    Promotion promotionEsperado = crearPromotion(1, "Papelería");

    // MOCKS
    when(promotionStorage.findPromotionById(promotionIdInput)).thenReturn(promotionEsperado);

    // EJECUCIÓN
    Promotion res = selectPromotionByIdUseCaseImpl.selectPromotionById(promotionIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(promotionEsperado, res);
        });
    verify(promotionStorage).findPromotionById(promotionIdInput);
  }

  @Test
  @DisplayName("Test caso de uso selectPromotionById Ko")
  void selectPromotionByIdKoTest() {

    // INPUT
    Integer promotionIdInput = 99;

    // MOCKS
    when(promotionStorage.findPromotionById(promotionIdInput))
        .thenThrow(new PromotionNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    PromotionNotFoundException ex =
        assertThrows(
            PromotionNotFoundException.class,
            () -> selectPromotionByIdUseCaseImpl.selectPromotionById(promotionIdInput));

    assertEquals("No se encuentra la promoción con id: 99", ex.getMessage());
  }
}
