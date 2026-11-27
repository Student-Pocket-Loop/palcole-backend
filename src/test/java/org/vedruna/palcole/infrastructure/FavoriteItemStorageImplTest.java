package org.vedruna.palcole.infrastructure;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.vedruna.palcole.domain.exception.favoriteItems.FavoriteItemAlreadyExistsException;
import org.vedruna.palcole.domain.exception.favoriteItems.FavoriteItemNotFoundException;
import org.vedruna.palcole.domain.model.FavoriteItem;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.FavoriteItemOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.FavoriteItemEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.FavoriteItemId;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.FavoriteItemRepository;

@ExtendWith(MockitoExtension.class)
class FavoriteItemStorageImplTest {

  @Mock private FavoriteItemRepository favoriteItemRepository;

  @Mock private FavoriteItemOutConverter favoriteItemOutConverter;

  @InjectMocks private FavoriteItemStorageImpl favoriteItemStorageImpl;

  private FavoriteItem crearFavoriteItem(Integer userId, Integer itemId) {
    FavoriteItem o = new FavoriteItem();
    o.setUserId(userId);
    o.setItemId(itemId);
    return o;
  }

  private FavoriteItemEntity crearFavoriteItemEntity(Integer userId, Integer itemId) {
    FavoriteItemEntity o = new FavoriteItemEntity();
    o.setUserId(userId);
    o.setItemId(itemId);
    return o;
  }

  @Test
  @DisplayName("Test storage selectAllFavoriteItems Ok")
  void selectAllFavoriteItemsOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<FavoriteItem> pageModelEsperado =
        new PageModel<>(
            List.of(crearFavoriteItem(1, 10), crearFavoriteItem(2, 20)), 2, 1, 2, 10, 0);

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);
    Page<FavoriteItemEntity> pageEntityMock =
        new PageImpl<>(
            List.of(crearFavoriteItemEntity(1, 10), crearFavoriteItemEntity(2, 20)),
            pageableMock,
            2);
    PageModel<FavoriteItem> pageModelMock =
        new PageModel<>(
            List.of(crearFavoriteItem(1, 10), crearFavoriteItem(2, 20)), 2, 1, 2, 10, 0);

    when(favoriteItemOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(favoriteItemRepository.findAll(pageableMock)).thenReturn(pageEntityMock);
    when(favoriteItemOutConverter.toFavoriteItemPageModel(pageEntityMock))
        .thenReturn(pageModelMock);

    // EJECUCIÓN
    PageModel<FavoriteItem> res =
        favoriteItemStorageImpl.selectAllFavoriteItems(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage selectAllFavoriteItems Ko")
  void selectAllFavoriteItemsKoTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "INVALID_SORT");

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);

    when(favoriteItemOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(favoriteItemRepository.findAll(pageableMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> favoriteItemStorageImpl.selectAllFavoriteItems(pageableModelInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(favoriteItemOutConverter, never()).toFavoriteItemPageModel(any());
  }

  @Test
  @DisplayName("Test storage findFavoriteItemById Ok")
  void findFavoriteItemByIdOkTest() {

    // INPUT
    Integer userIdInput = 1;
    Integer itemIdInput = 10;

    // ESPERADO
    FavoriteItem favoriteItemEsperado = crearFavoriteItem(1, 10);

    // MOCKS
    FavoriteItemEntity favoriteItemEntityMock = crearFavoriteItemEntity(1, 10);
    FavoriteItem favoriteItemMock = crearFavoriteItem(1, 10);

    when(favoriteItemRepository.findById(new FavoriteItemId(userIdInput, itemIdInput)))
        .thenReturn(Optional.of(favoriteItemEntityMock));
    when(favoriteItemOutConverter.toDomain(favoriteItemEntityMock)).thenReturn(favoriteItemMock);

    // EJECUCIÓN
    FavoriteItem res = favoriteItemStorageImpl.findFavoriteItemById(userIdInput, itemIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(favoriteItemEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage findFavoriteItemById Ko")
  void findFavoriteItemByIdKoTest() {

    // INPUT
    Integer userIdInput = 99;
    Integer itemIdInput = 98;

    // MOCKS
    when(favoriteItemRepository.findById(new FavoriteItemId(userIdInput, itemIdInput)))
        .thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    FavoriteItemNotFoundException ex =
        assertThrows(
            FavoriteItemNotFoundException.class,
            () -> favoriteItemStorageImpl.findFavoriteItemById(userIdInput, itemIdInput));

    assertEquals(
        "No se encuentra el favorito de artículo con userId: 99 e itemId: 98", ex.getMessage());
    verify(favoriteItemOutConverter, never()).toDomain(any(FavoriteItemEntity.class));
  }

  @Test
  @DisplayName("Test storage insertFavoriteItem Ok")
  void insertFavoriteItemOkTest() {

    // INPUT
    FavoriteItem newFavoriteItemInput = crearFavoriteItem(1, 10);

    // ESPERADO
    FavoriteItem favoriteItemEsperado = crearFavoriteItem(1, 10);

    // MOCKS
    FavoriteItemEntity favoriteItemEntityNuevoMock = crearFavoriteItemEntity(1, 10);
    FavoriteItemEntity favoriteItemEntityGuardadoMock = crearFavoriteItemEntity(1, 10);
    FavoriteItem favoriteItemCreadoMock = crearFavoriteItem(1, 10);

    when(favoriteItemRepository.existsById(new FavoriteItemId(1, 10))).thenReturn(false);
    when(favoriteItemOutConverter.toEntity(newFavoriteItemInput))
        .thenReturn(favoriteItemEntityNuevoMock);
    when(favoriteItemRepository.save(favoriteItemEntityNuevoMock))
        .thenReturn(favoriteItemEntityGuardadoMock);
    when(favoriteItemOutConverter.toDomain(favoriteItemEntityGuardadoMock))
        .thenReturn(favoriteItemCreadoMock);

    // EJECUCIÓN
    FavoriteItem res = favoriteItemStorageImpl.insertFavoriteItem(newFavoriteItemInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(favoriteItemEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage insertFavoriteItem Ko")
  void insertFavoriteItemKoTest() {

    // INPUT
    FavoriteItem newFavoriteItemInput = crearFavoriteItem(1, 10);

    // MOCKS
    when(favoriteItemRepository.existsById(new FavoriteItemId(1, 10))).thenReturn(true);

    // EJECUCIÓN Y VALIDACIONES
    FavoriteItemAlreadyExistsException ex =
        assertThrows(
            FavoriteItemAlreadyExistsException.class,
            () -> favoriteItemStorageImpl.insertFavoriteItem(newFavoriteItemInput));

    assertTrue(ex.getMessage().startsWith("Ya existe el favorito de artículo"));
    verify(favoriteItemRepository, never()).save(any(FavoriteItemEntity.class));
    verify(favoriteItemOutConverter, never()).toDomain(any(FavoriteItemEntity.class));
  }

  @Test
  @DisplayName("Test storage deleteFavoriteItem Ok")
  void deleteFavoriteItemOkTest() {

    // INPUT
    Integer userIdInput = 1;
    Integer itemIdInput = 10;

    // MOCKS
    when(favoriteItemRepository.existsById(new FavoriteItemId(userIdInput, itemIdInput)))
        .thenReturn(true);
    doNothing()
        .when(favoriteItemRepository)
        .deleteById(new FavoriteItemId(userIdInput, itemIdInput));

    // EJECUCIÓN
    favoriteItemStorageImpl.deleteFavoriteItem(userIdInput, itemIdInput);

    // VALIDACIONES
    verify(favoriteItemRepository).existsById(new FavoriteItemId(userIdInput, itemIdInput));
    verify(favoriteItemRepository).deleteById(new FavoriteItemId(userIdInput, itemIdInput));
  }

  @Test
  @DisplayName("Test storage deleteFavoriteItem Ko")
  void deleteFavoriteItemKoTest() {

    // INPUT
    Integer userIdInput = 99;
    Integer itemIdInput = 98;

    // MOCKS
    when(favoriteItemRepository.existsById(new FavoriteItemId(userIdInput, itemIdInput)))
        .thenReturn(false);

    // EJECUCIÓN Y VALIDACIONES
    FavoriteItemNotFoundException ex =
        assertThrows(
            FavoriteItemNotFoundException.class,
            () -> favoriteItemStorageImpl.deleteFavoriteItem(userIdInput, itemIdInput));

    assertEquals(
        "No se encuentra el favorito de artículo con userId: 99 e itemId: 98", ex.getMessage());
    verify(favoriteItemRepository, never()).deleteById(any());
  }
}
