package org.vedruna.palcole.application.favouriteitems;

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
import org.vedruna.palcole.domain.exception.favoriteItems.FavoriteItemNotFoundException;
import org.vedruna.palcole.domain.model.FavoriteItem;
import org.vedruna.palcole.domain.ports.outbound.FavoriteItemStorage;

@ExtendWith(MockitoExtension.class)
class SelectFavoriteItemByIdUseCaseImplTest {

  @Mock private FavoriteItemStorage favoriteItemStorage;

  @InjectMocks private SelectFavoriteItemByIdUseCaseImpl selectFavoriteItemByIdUseCaseImpl;

  private FavoriteItem crearFavoriteItem(Integer userId, Integer itemId) {
    FavoriteItem o = new FavoriteItem();
    o.setUserId(userId);
    o.setItemId(itemId);
    return o;
  }

  @Test
  @DisplayName("Test caso de uso selectFavoriteItemById Ok")
  void selectFavoriteItemByIdOkTest() {

    // INPUT
    Integer userIdInput = 1;
    Integer itemIdInput = 10;

    // ESPERADO
    FavoriteItem favoriteItemEsperado = crearFavoriteItem(1, 10);

    // MOCKS
    when(favoriteItemStorage.findFavoriteItemById(userIdInput, itemIdInput))
        .thenReturn(favoriteItemEsperado);

    // EJECUCIÓN
    FavoriteItem res =
        selectFavoriteItemByIdUseCaseImpl.selectFavoriteItemById(userIdInput, itemIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(favoriteItemEsperado, res);
        });
    verify(favoriteItemStorage).findFavoriteItemById(userIdInput, itemIdInput);
  }

  @Test
  @DisplayName("Test caso de uso selectFavoriteItemById Ko")
  void selectFavoriteItemByIdKoTest() {

    // INPUT
    Integer userIdInput = 99;
    Integer itemIdInput = 98;

    // MOCKS
    when(favoriteItemStorage.findFavoriteItemById(userIdInput, itemIdInput))
        .thenThrow(new FavoriteItemNotFoundException(99, 98));

    // EJECUCIÓN Y VALIDACIONES
    FavoriteItemNotFoundException ex =
        assertThrows(
            FavoriteItemNotFoundException.class,
            () ->
                selectFavoriteItemByIdUseCaseImpl.selectFavoriteItemById(userIdInput, itemIdInput));

    assertEquals(
        "No se encuentra el favorito de artículo con userId: 99 e itemId: 98", ex.getMessage());
  }
}
