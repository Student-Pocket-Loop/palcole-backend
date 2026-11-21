package org.vedruna.palcole.application.favoritePromotions;

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
import org.vedruna.palcole.domain.exception.favoritePromotions.FavoritePromotionNotFoundException;
import org.vedruna.palcole.domain.ports.outbound.FavoritePromotionStorage;

@ExtendWith(MockitoExtension.class)
class DeleteFavoritePromotionUseCaseImplTest {

  @Mock private FavoritePromotionStorage favoritePromotionStorage;

  @InjectMocks private DeleteFavoritePromotionUseCaseImpl deleteFavoritePromotionUseCaseImpl;

  @Test
  @DisplayName("Test caso de uso deleteFavoritePromotion Ok")
  void deleteFavoritePromotionOkTest() {

    // INPUT
    Integer userIdInput = 1;
    Integer promotionIdInput = 10;

    // MOCKS
    doNothing()
        .when(favoritePromotionStorage)
        .deleteFavoritePromotion(userIdInput, promotionIdInput);

    // EJECUCIÓN Y VALIDACIONES
    assertDoesNotThrow(
        () ->
            deleteFavoritePromotionUseCaseImpl.deleteFavoritePromotion(
                userIdInput, promotionIdInput));
    verify(favoritePromotionStorage).deleteFavoritePromotion(userIdInput, promotionIdInput);
  }

  @Test
  @DisplayName("Test caso de uso deleteFavoritePromotion Ko")
  void deleteFavoritePromotionKoTest() {

    // INPUT
    Integer userIdInput = 99;
    Integer promotionIdInput = 98;

    // MOCKS
    doThrow(new FavoritePromotionNotFoundException(99, 98))
        .when(favoritePromotionStorage)
        .deleteFavoritePromotion(userIdInput, promotionIdInput);

    // EJECUCIÓN Y VALIDACIONES
    FavoritePromotionNotFoundException ex =
        assertThrows(
            FavoritePromotionNotFoundException.class,
            () ->
                deleteFavoritePromotionUseCaseImpl.deleteFavoritePromotion(
                    userIdInput, promotionIdInput));

    assertEquals(
        "No se encuentra el favorito de promoción con userId: 99 e promotionId: 98",
        ex.getMessage());
  }
}
