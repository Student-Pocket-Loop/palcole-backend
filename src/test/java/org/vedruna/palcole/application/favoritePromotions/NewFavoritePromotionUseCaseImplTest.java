package org.vedruna.palcole.application.favoritePromotions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.exception.favoritePromotions.FavoritePromotionAlreadyExistsException;
import org.vedruna.palcole.domain.model.FavoritePromotion;
import org.vedruna.palcole.domain.ports.outbound.FavoritePromotionStorage;

@ExtendWith(MockitoExtension.class)
class NewFavoritePromotionUseCaseImplTest {

  @Mock private FavoritePromotionStorage favoritePromotionStorage;

  @InjectMocks private NewFavoritePromotionUseCaseImpl newFavoritePromotionUseCaseImpl;

  private FavoritePromotion crearFavoritePromotion(Integer userId, Integer promotionId) {
    FavoritePromotion o = new FavoritePromotion();
    o.setUserId(userId);
    o.setPromotionId(promotionId);
    return o;
  }

  @Test
  @DisplayName("Test caso de uso newFavoritePromotion Ok")
  void newFavoritePromotionOkTest() {

    // INPUT
    FavoritePromotion newFavoritePromotionInput = crearFavoritePromotion(1, 10);

    // ESPERADO
    FavoritePromotion favoritePromotionEsperado = crearFavoritePromotion(1, 10);

    // MOCKS
    FavoritePromotion favoritePromotionCreadoMock = crearFavoritePromotion(1, 10);

    when(favoritePromotionStorage.insertFavoritePromotion(newFavoritePromotionInput))
        .thenReturn(favoritePromotionCreadoMock);

    // EJECUCIÓN
    FavoritePromotion res =
        newFavoritePromotionUseCaseImpl.newFavoritePromotion(newFavoritePromotionInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(favoritePromotionEsperado, res);
        });
    verify(favoritePromotionStorage).insertFavoritePromotion(newFavoritePromotionInput);
  }

  @Test
  @DisplayName("Test caso de uso newFavoritePromotion Ko")
  void newFavoritePromotionKoTest() {

    // INPUT
    FavoritePromotion newFavoritePromotionInput = crearFavoritePromotion(1, 10);

    // MOCKS
    when(favoritePromotionStorage.insertFavoritePromotion(newFavoritePromotionInput))
        .thenThrow(new FavoritePromotionAlreadyExistsException(1, 10));

    // EJECUCIÓN Y VALIDACIONES
    FavoritePromotionAlreadyExistsException ex =
        assertThrows(
            FavoritePromotionAlreadyExistsException.class,
            () -> newFavoritePromotionUseCaseImpl.newFavoritePromotion(newFavoritePromotionInput));

    assertNotNull(ex.getMessage());
    verify(favoritePromotionStorage).insertFavoritePromotion(newFavoritePromotionInput);
  }
}
