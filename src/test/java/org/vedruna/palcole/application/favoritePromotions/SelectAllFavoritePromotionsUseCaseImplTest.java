package org.vedruna.palcole.application.favoritePromotions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.model.FavoritePromotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.FavoritePromotionStorage;

@ExtendWith(MockitoExtension.class)
class SelectAllFavoritePromotionsUseCaseImplTest {

  @Mock private FavoritePromotionStorage favoritePromotionStorage;

  @InjectMocks
  private SelectAllFavoritePromotionsUseCaseImpl selectAllFavoritePromotionsUseCaseImpl;

  private FavoritePromotion crearFavoritePromotion(Integer userId, Integer promotionId) {
    FavoritePromotion o = new FavoritePromotion();
    o.setUserId(userId);
    o.setPromotionId(promotionId);
    return o;
  }

  @Test
  @DisplayName("Test caso de uso selectAllFavoritePromotions Ok")
  void selectAllFavoritePromotionsOkTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<FavoritePromotion> pageEsperado =
        new PageModel<>(
            List.of(crearFavoritePromotion(1, 10), crearFavoritePromotion(2, 20)), 2, 1, 2, 10, 0);

    // MOCKS
    when(favoritePromotionStorage.selectAllFavoritePromotions(pageableInput))
        .thenReturn(pageEsperado);

    // EJECUCIÓN
    PageModel<FavoritePromotion> res =
        selectAllFavoritePromotionsUseCaseImpl.selectAllFavoritePromotions(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(2, res.getTotalElements());
          assertEquals(pageEsperado, res);
        });
    verify(favoritePromotionStorage).selectAllFavoritePromotions(pageableInput);
  }

  @Test
  @DisplayName("Test caso de uso selectAllFavoritePromotions Ko")
  void selectAllFavoritePromotionsKoTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "inexistente: ASC");

    // MOCKS
    when(favoritePromotionStorage.selectAllFavoritePromotions(pageableInput))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                selectAllFavoritePromotionsUseCaseImpl.selectAllFavoritePromotions(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
  }
}
