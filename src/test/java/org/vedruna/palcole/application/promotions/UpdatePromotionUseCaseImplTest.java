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
import org.vedruna.palcole.domain.exception.promotions.PromotionNotFoundException;
import org.vedruna.palcole.domain.model.Promotion;
import org.vedruna.palcole.domain.ports.outbound.PromotionStorage;

@ExtendWith(MockitoExtension.class)
class UpdatePromotionUseCaseImplTest {

  @Mock private PromotionStorage promotionStorage;

  @InjectMocks private UpdatePromotionUseCaseImpl updatePromotionUseCaseImpl;

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
  @DisplayName("Test caso de uso updatePromotion Ok")
  void updatePromotionOkTest() {

    // INPUT
    Integer promotionIdInput = 1;
    Promotion newPromotionInput = crearPromotion(null, "Descuento actualizado");

    // ESPERADO
    Promotion promotionEsperado = crearPromotion(1, "Descuento actualizado");

    // MOCKS
    Promotion promotionActualizadoMock = crearPromotion(1, "Descuento actualizado");

    when(promotionStorage.updatePromotion(promotionIdInput, newPromotionInput))
        .thenReturn(promotionActualizadoMock);

    // EJECUCIÓN
    Promotion res = updatePromotionUseCaseImpl.updatePromotion(promotionIdInput, newPromotionInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(promotionEsperado, res);
        });
    verify(promotionStorage).updatePromotion(promotionIdInput, newPromotionInput);
  }

  @Test
  @DisplayName("Test caso de uso updatePromotion Ko")
  void updatePromotionKoTest() {

    // INPUT
    Integer promotionIdInput = 99;
    Promotion newPromotionInput = crearPromotion(null, "Descuento actualizado");

    // MOCKS
    when(promotionStorage.updatePromotion(promotionIdInput, newPromotionInput))
        .thenThrow(new PromotionNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    PromotionNotFoundException ex =
        assertThrows(
            PromotionNotFoundException.class,
            () -> updatePromotionUseCaseImpl.updatePromotion(promotionIdInput, newPromotionInput));

    assertEquals("No se encuentra la promoción con id: 99", ex.getMessage());
    verify(promotionStorage).updatePromotion(promotionIdInput, newPromotionInput);
  }

  @Test
  @DisplayName("Test caso de uso updatePromotion Ko (fecha de fin anterior a la de inicio)")
  void updatePromotionKoFechasTest() {

    // INPUT
    Integer promotionIdInput = 1;
    Promotion newPromotionInput = crearPromotion(null, "Papelería");
    newPromotionInput.setEndDate(LocalDate.of(2026, 8, 1));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> updatePromotionUseCaseImpl.updatePromotion(promotionIdInput, newPromotionInput));

    assertEquals("La fecha de fin no puede ser anterior a la fecha de inicio", ex.getMessage());
    verify(promotionStorage, never()).updatePromotion(any(), any(Promotion.class));
  }

  @Test
  @DisplayName("Test caso de uso updatePromotion Ko (precio con descuento mayor al original)")
  void updatePromotionKoPrecioTest() {

    // INPUT
    Integer promotionIdInput = 1;
    Promotion newPromotionInput = crearPromotion(null, "Papelería");
    newPromotionInput.setDiscountPrice(new BigDecimal("25.00"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> updatePromotionUseCaseImpl.updatePromotion(promotionIdInput, newPromotionInput));

    assertEquals("El precio con descuento no puede superar al precio original", ex.getMessage());
    verify(promotionStorage, never()).updatePromotion(any(), any(Promotion.class));
  }
}
