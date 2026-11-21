package org.vedruna.palcole.application.promotions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.vedruna.palcole.domain.model.Promotion;
import org.vedruna.palcole.domain.ports.outbound.PromotionStorage;

@ExtendWith(MockitoExtension.class)
class NewPromotionUseCaseImplTest {

  @Mock private PromotionStorage promotionStorage;

  @InjectMocks private NewPromotionUseCaseImpl newPromotionUseCaseImpl;

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
  @DisplayName("Test caso de uso newPromotion Ok")
  void newPromotionOkTest() {

    // INPUT
    Promotion newPromotionInput = crearPromotion(null, "Papelería");

    // ESPERADO
    Promotion promotionEsperado = crearPromotion(3, "Papelería");

    // MOCKS
    Promotion promotionCreadoMock = crearPromotion(3, "Papelería");

    when(promotionStorage.insertPromotion(newPromotionInput)).thenReturn(promotionCreadoMock);

    // EJECUCIÓN
    Promotion res = newPromotionUseCaseImpl.newPromotion(newPromotionInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(promotionEsperado, res);
        });
    verify(promotionStorage).insertPromotion(newPromotionInput);
  }

  @Test
  @DisplayName("Test caso de uso newPromotion Ko")
  void newPromotionKoTest() {

    // INPUT
    Promotion newPromotionInput = crearPromotion(null, "Papelería");

    // MOCKS
    when(promotionStorage.insertPromotion(newPromotionInput))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> newPromotionUseCaseImpl.newPromotion(newPromotionInput));

    assertNotNull(ex.getMessage());
    verify(promotionStorage).insertPromotion(newPromotionInput);
  }

  @Test
  @DisplayName("Test caso de uso newPromotion Ko (fecha de fin anterior a la de inicio)")
  void newPromotionKoFechasTest() {

    // INPUT
    Promotion newPromotionInput = crearPromotion(null, "Papelería");
    newPromotionInput.setEndDate(LocalDate.of(2026, 8, 1));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> newPromotionUseCaseImpl.newPromotion(newPromotionInput));

    assertEquals("La fecha de fin no puede ser anterior a la fecha de inicio", ex.getMessage());
    verify(promotionStorage, never()).insertPromotion(any(Promotion.class));
  }

  @Test
  @DisplayName("Test caso de uso newPromotion Ko (precio con descuento mayor al original)")
  void newPromotionKoPrecioTest() {

    // INPUT
    Promotion newPromotionInput = crearPromotion(null, "Papelería");
    newPromotionInput.setDiscountPrice(new BigDecimal("25.00"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> newPromotionUseCaseImpl.newPromotion(newPromotionInput));

    assertEquals("El precio con descuento no puede superar al precio original", ex.getMessage());
    verify(promotionStorage, never()).insertPromotion(any(Promotion.class));
  }
}
