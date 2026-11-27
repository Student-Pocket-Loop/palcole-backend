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
import org.vedruna.palcole.domain.exception.favoriteItems.FavoriteItemAlreadyExistsException;
import org.vedruna.palcole.domain.model.FavoriteItem;
import org.vedruna.palcole.domain.ports.outbound.FavoriteItemStorage;

@ExtendWith(MockitoExtension.class)
class NewFavoriteItemUseCaseImplTest {

  @Mock private FavoriteItemStorage favoriteItemStorage;

  @InjectMocks private NewFavoriteItemUseCaseImpl newFavoriteItemUseCaseImpl;

  private FavoriteItem crearFavoriteItem(Integer userId, Integer itemId) {
    FavoriteItem o = new FavoriteItem();
    o.setUserId(userId);
    o.setItemId(itemId);
    return o;
  }

  @Test
  @DisplayName("Test caso de uso newFavoriteItem Ok")
  void newFavoriteItemOkTest() {

    // INPUT
    FavoriteItem newFavoriteItemInput = crearFavoriteItem(1, 10);

    // ESPERADO
    FavoriteItem favoriteItemEsperado = crearFavoriteItem(1, 10);

    // MOCKS
    FavoriteItem favoriteItemCreadoMock = crearFavoriteItem(1, 10);

    when(favoriteItemStorage.insertFavoriteItem(newFavoriteItemInput))
        .thenReturn(favoriteItemCreadoMock);

    // EJECUCIÓN
    FavoriteItem res = newFavoriteItemUseCaseImpl.newFavoriteItem(newFavoriteItemInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(favoriteItemEsperado, res);
        });
    verify(favoriteItemStorage).insertFavoriteItem(newFavoriteItemInput);
  }

  @Test
  @DisplayName("Test caso de uso newFavoriteItem Ko")
  void newFavoriteItemKoTest() {

    // INPUT
    FavoriteItem newFavoriteItemInput = crearFavoriteItem(1, 10);

    // MOCKS
    when(favoriteItemStorage.insertFavoriteItem(newFavoriteItemInput))
        .thenThrow(new FavoriteItemAlreadyExistsException(1, 10));

    // EJECUCIÓN Y VALIDACIONES
    FavoriteItemAlreadyExistsException ex =
        assertThrows(
            FavoriteItemAlreadyExistsException.class,
            () -> newFavoriteItemUseCaseImpl.newFavoriteItem(newFavoriteItemInput));

    assertNotNull(ex.getMessage());
    verify(favoriteItemStorage).insertFavoriteItem(newFavoriteItemInput);
  }
}
