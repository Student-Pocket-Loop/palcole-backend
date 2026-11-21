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
import org.vedruna.palcole.domain.exception.favoritePromotions.FavoritePromotionNotFoundException;
import org.vedruna.palcole.domain.model.FavoritePromotion;
import org.vedruna.palcole.domain.ports.outbound.FavoritePromotionStorage;

@ExtendWith(MockitoExtension.class)
class SelectFavoritePromotionByIdUseCaseImplTest {

  @Mock private FavoritePromotionStorage favoritePromotionStorage;

  @InjectMocks
  private SelectFavoritePromotionByIdUseCaseImpl selectFavoritePromotionByIdUseCaseImpl;

  private FavoritePromotion crearFavoritePromotion(Integer userId, Integer promotionId) {
    FavoritePromotion o = new FavoritePromotion();
    o.setUserId(userId);
    o.setPromotionId(promotionId);
    return o;
  }

  @Test
  @DisplayName("Test caso de uso selectFavoritePromotionById Ok")
  void selectFavoritePromotionByIdOkTest() {

    // INPUT
    Integer userIdInput = 1;
    Integer promotionIdInput = 10;

    // ESPERADO
    FavoritePromotion favoritePromotionEsperado = crearFavoritePromotion(1, 10);

    // MOCKS
    when(favoritePromotionStorage.findFavoritePromotionById(userIdInput, promotionIdInput))
        .thenReturn(favoritePromotionEsperado);

    // EJECUCIÓN
    FavoritePromotion res =
        selectFavoritePromotionByIdUseCaseImpl.selectFavoritePromotionById(
            userIdInput, promotionIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(favoritePromotionEsperado, res);
        });
    verify(favoritePromotionStorage).findFavoritePromotionById(userIdInput, promotionIdInput);
  }

  @Test
  @DisplayName("Test caso de uso selectFavoritePromotionById Ko")
  void selectFavoritePromotionByIdKoTest() {

    // INPUT
    Integer userIdInput = 99;
    Integer promotionIdInput = 98;

    // MOCKS
    when(favoritePromotionStorage.findFavoritePromotionById(userIdInput, promotionIdInput))
        .thenThrow(new FavoritePromotionNotFoundException(99, 98));

    // EJECUCIÓN Y VALIDACIONES
    FavoritePromotionNotFoundException ex =
        assertThrows(
            FavoritePromotionNotFoundException.class,
            () ->
                selectFavoritePromotionByIdUseCaseImpl.selectFavoritePromotionById(
                    userIdInput, promotionIdInput));

    assertEquals(
        "No se encuentra el favorito de promoción con userId: 99 e promotionId: 98",
        ex.getMessage());
  }
}
