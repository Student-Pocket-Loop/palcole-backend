package org.vedruna.palcole.application.promotions;

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
import org.vedruna.palcole.domain.exception.promotions.PromotionNotFoundException;
import org.vedruna.palcole.domain.ports.outbound.PromotionStorage;

@ExtendWith(MockitoExtension.class)
class DeletePromotionUseCaseImplTest {

  @Mock private PromotionStorage promotionStorage;

  @InjectMocks private DeletePromotionUseCaseImpl deletePromotionUseCaseImpl;

  @Test
  @DisplayName("Test caso de uso deletePromotion Ok")
  void deletePromotionOkTest() {

    // INPUT
    Integer promotionIdInput = 1;

    // MOCKS
    doNothing().when(promotionStorage).deletePromotion(promotionIdInput);

    // EJECUCIÓN Y VALIDACIONES
    assertDoesNotThrow(() -> deletePromotionUseCaseImpl.deletePromotion(promotionIdInput));
    verify(promotionStorage).deletePromotion(promotionIdInput);
  }

  @Test
  @DisplayName("Test caso de uso deletePromotion Ko")
  void deletePromotionKoTest() {

    // INPUT
    Integer promotionIdInput = 99;

    // MOCKS
    doThrow(new PromotionNotFoundException(99))
        .when(promotionStorage)
        .deletePromotion(promotionIdInput);

    // EJECUCIÓN Y VALIDACIONES
    PromotionNotFoundException ex =
        assertThrows(
            PromotionNotFoundException.class,
            () -> deletePromotionUseCaseImpl.deletePromotion(promotionIdInput));

    assertEquals("No se encuentra la promoción con id: 99", ex.getMessage());
  }
}
