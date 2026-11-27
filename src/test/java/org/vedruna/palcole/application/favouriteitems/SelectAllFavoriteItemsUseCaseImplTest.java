package org.vedruna.palcole.application.favouriteitems;

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
import org.vedruna.palcole.domain.model.FavoriteItem;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.FavoriteItemStorage;

@ExtendWith(MockitoExtension.class)
class SelectAllFavoriteItemsUseCaseImplTest {

  @Mock private FavoriteItemStorage favoriteItemStorage;

  @InjectMocks private SelectAllFavoriteItemsUseCaseImpl selectAllFavoriteItemsUseCaseImpl;

  private FavoriteItem crearFavoriteItem(Integer userId, Integer itemId) {
    FavoriteItem o = new FavoriteItem();
    o.setUserId(userId);
    o.setItemId(itemId);
    return o;
  }

  @Test
  @DisplayName("Test caso de uso selectAllFavoriteItems Ok")
  void selectAllFavoriteItemsOkTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<FavoriteItem> pageEsperado =
        new PageModel<>(
            List.of(crearFavoriteItem(1, 10), crearFavoriteItem(2, 20)), 2, 1, 2, 10, 0);

    // MOCKS
    when(favoriteItemStorage.selectAllFavoriteItems(pageableInput)).thenReturn(pageEsperado);

    // EJECUCIÓN
    PageModel<FavoriteItem> res =
        selectAllFavoriteItemsUseCaseImpl.selectAllFavoriteItems(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(2, res.getTotalElements());
          assertEquals(pageEsperado, res);
        });
    verify(favoriteItemStorage).selectAllFavoriteItems(pageableInput);
  }

  @Test
  @DisplayName("Test caso de uso selectAllFavoriteItems Ko")
  void selectAllFavoriteItemsKoTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "inexistente: ASC");

    // MOCKS
    when(favoriteItemStorage.selectAllFavoriteItems(pageableInput))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> selectAllFavoriteItemsUseCaseImpl.selectAllFavoriteItems(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
  }
}
