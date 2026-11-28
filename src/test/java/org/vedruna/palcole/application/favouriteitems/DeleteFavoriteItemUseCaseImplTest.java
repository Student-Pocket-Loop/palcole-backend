package org.vedruna.palcole.application.favouriteitems;

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
import org.vedruna.palcole.domain.exception.favoriteItems.FavoriteItemNotFoundException;
import org.vedruna.palcole.domain.ports.outbound.FavoriteItemStorage;

@ExtendWith(MockitoExtension.class)
class DeleteFavoriteItemUseCaseImplTest {

  @Mock private FavoriteItemStorage favoriteItemStorage;

  @InjectMocks private DeleteFavoriteItemUseCaseImpl deleteFavoriteItemUseCaseImpl;

  @Test
  @DisplayName("Test caso de uso deleteFavoriteItem Ok")
  void deleteFavoriteItemOkTest() {

    // INPUT
    Integer userIdInput = 1;
    Integer itemIdInput = 10;

    // MOCKS
    doNothing().when(favoriteItemStorage).deleteFavoriteItem(userIdInput, itemIdInput);

    // EJECUCIÓN Y VALIDACIONES
    assertDoesNotThrow(
        () -> deleteFavoriteItemUseCaseImpl.deleteFavoriteItem(userIdInput, itemIdInput));
    verify(favoriteItemStorage).deleteFavoriteItem(userIdInput, itemIdInput);
  }

  @Test
  @DisplayName("Test caso de uso deleteFavoriteItem Ko")
  void deleteFavoriteItemKoTest() {

    // INPUT
    Integer userIdInput = 99;
    Integer itemIdInput = 98;

    // MOCKS
    doThrow(new FavoriteItemNotFoundException(99, 98))
        .when(favoriteItemStorage)
        .deleteFavoriteItem(userIdInput, itemIdInput);

    // EJECUCIÓN Y VALIDACIONES
    FavoriteItemNotFoundException ex =
        assertThrows(
            FavoriteItemNotFoundException.class,
            () -> deleteFavoriteItemUseCaseImpl.deleteFavoriteItem(userIdInput, itemIdInput));

    assertEquals(
        "No se encuentra el favorito de artículo con userId: 99 e itemId: 98", ex.getMessage());
  }
}
