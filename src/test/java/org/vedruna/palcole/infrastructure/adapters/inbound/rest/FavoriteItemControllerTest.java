package org.vedruna.palcole.infrastructure.adapters.inbound.rest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.vedruna.palcole.domain.exception.favoriteItems.FavoriteItemAlreadyExistsException;
import org.vedruna.palcole.domain.exception.favoriteItems.FavoriteItemNotFoundException;
import org.vedruna.palcole.domain.model.FavoriteItem;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.favouriteitems.DeleteFavoriteItemUseCase;
import org.vedruna.palcole.domain.ports.inbound.favouriteitems.NewFavoriteItemUseCase;
import org.vedruna.palcole.domain.ports.inbound.favouriteitems.SelectAllFavoriteItemsUseCase;
import org.vedruna.palcole.domain.ports.inbound.favouriteitems.SelectFavoriteItemByIdUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.FavoriteItemInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.favouriteitem.FavoriteItemDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.favouriteitem.InsertFavoriteItemDTO;

@ExtendWith(MockitoExtension.class)
class FavoriteItemControllerTest {

  @Mock private NewFavoriteItemUseCase newFavoriteItemUseCase;

  @Mock private DeleteFavoriteItemUseCase deleteFavoriteItemUseCase;

  @Mock private SelectAllFavoriteItemsUseCase selectAllFavoriteItemsUseCase;

  @Mock private SelectFavoriteItemByIdUseCase selectFavoriteItemByIdUseCase;

  @Mock private FavoriteItemInConverter favoriteItemInConverter;

  @InjectMocks private FavoriteItemController favoriteItemController;

  private FavoriteItem crearFavoriteItem(Integer userId, Integer itemId) {
    FavoriteItem o = new FavoriteItem();
    o.setUserId(userId);
    o.setItemId(itemId);
    return o;
  }

  private FavoriteItemDTO crearFavoriteItemDTO(Integer userId, Integer itemId) {
    FavoriteItemDTO o = new FavoriteItemDTO();
    o.setUserId(userId);
    o.setItemId(itemId);
    return o;
  }

  private InsertFavoriteItemDTO crearInsertFavoriteItemDTO(Integer userId, Integer itemId) {
    InsertFavoriteItemDTO o = new InsertFavoriteItemDTO();
    o.setUserId(userId);
    o.setItemId(itemId);
    return o;
  }

  @Test
  @DisplayName("Test controlador getAll Ok")
  void getAllOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10);

    // ESPERADO
    List<FavoriteItemDTO> lista = List.of(crearFavoriteItemDTO(1, 10), crearFavoriteItemDTO(2, 20));

    Pageable pageableEsperado = PageRequest.of(0, 10);

    Page<FavoriteItemDTO> pageEsperado = new PageImpl<>(lista, pageableEsperado, 2);

    ResponseEntity<Page<FavoriteItemDTO>> resEsperado = ResponseEntity.ok(pageEsperado);

    // MOCKS
    // CONVERTER MOCK (Pageable -> PageableModel)
    PageableModel pageableModelMock = new PageableModel(0, 10, "UNSORTED");

    when(favoriteItemInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);

    // USE CASE MOCK
    PageModel<FavoriteItem> pageModelMock =
        new PageModel<>(
            List.of(crearFavoriteItem(1, 10), crearFavoriteItem(2, 20)), 2, 1, 2, 10, 0);

    when(selectAllFavoriteItemsUseCase.selectAllFavoriteItems(pageableModelMock))
        .thenReturn(pageModelMock);

    // CONVERTER MOCK (PageModel -> Page de DTOs)
    Page<FavoriteItemDTO> pageDtoMock =
        new PageImpl<>(
            List.of(crearFavoriteItemDTO(1, 10), crearFavoriteItemDTO(2, 20)),
            PageRequest.of(0, 10),
            2);

    when(favoriteItemInConverter.toFavoriteItemsPage(pageModelMock, pageableModelMock))
        .thenReturn(pageDtoMock);

    // EJECUCIÓN
    ResponseEntity<Page<FavoriteItemDTO>> res =
        favoriteItemController.getAllFavoriteItems(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.OK, res.getStatusCode());
          assertEquals(2, res.getBody().getContent().size());
          assertEquals(pageEsperado, res.getBody());
          assertEquals(resEsperado, res);
        });
  }

  @Test
  @DisplayName("Test controlador getAll Ko")
  void getAllKoTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10);

    // MOCKS
    PageableModel pageableModelMock = new PageableModel(0, 10, "UNSORTED");

    when(favoriteItemInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);
    when(selectAllFavoriteItemsUseCase.selectAllFavoriteItems(pageableModelMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> favoriteItemController.getAllFavoriteItems(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(favoriteItemInConverter, never()).toFavoriteItemsPage(any(), any());
  }

  @Test
  @DisplayName("Test controlador insertIntoFavoriteItem Ok")
  void insertIntoFavoriteItemOkTest() {

    // INPUT
    InsertFavoriteItemDTO insertFavoriteItemDTOInput = crearInsertFavoriteItemDTO(1, 10);

    // ESPERADO
    FavoriteItemDTO dtoEsperado = crearFavoriteItemDTO(1, 10);

    ResponseEntity<FavoriteItemDTO> resEsperado =
        ResponseEntity.status(HttpStatus.CREATED).body(dtoEsperado);

    // MOCKS
    FavoriteItem favoriteItemNuevoMock = crearFavoriteItem(1, 10);
    FavoriteItem favoriteItemCreadoMock = crearFavoriteItem(1, 10);

    when(favoriteItemInConverter.toDomainInsert(insertFavoriteItemDTOInput))
        .thenReturn(favoriteItemNuevoMock);
    when(newFavoriteItemUseCase.newFavoriteItem(favoriteItemNuevoMock))
        .thenReturn(favoriteItemCreadoMock);
    when(favoriteItemInConverter.toDto(favoriteItemCreadoMock))
        .thenReturn(crearFavoriteItemDTO(1, 10));

    // EJECUCIÓN
    ResponseEntity<FavoriteItemDTO> res =
        favoriteItemController.insertIntoFavoriteItem(insertFavoriteItemDTOInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.CREATED, res.getStatusCode());
          assertEquals(dtoEsperado, res.getBody());
          assertEquals(resEsperado, res);
        });
  }

  @Test
  @DisplayName("Test controlador insertIntoFavoriteItem Ko")
  void insertIntoFavoriteItemKoTest() {

    // INPUT
    InsertFavoriteItemDTO insertFavoriteItemDTOInput = crearInsertFavoriteItemDTO(1, 10);

    // MOCKS
    FavoriteItem favoriteItemNuevoMock = crearFavoriteItem(1, 10);

    when(favoriteItemInConverter.toDomainInsert(insertFavoriteItemDTOInput))
        .thenReturn(favoriteItemNuevoMock);
    when(newFavoriteItemUseCase.newFavoriteItem(favoriteItemNuevoMock))
        .thenThrow(new FavoriteItemAlreadyExistsException(1, 10));

    // EJECUCIÓN Y VALIDACIONES
    FavoriteItemAlreadyExistsException ex =
        assertThrows(
            FavoriteItemAlreadyExistsException.class,
            () -> favoriteItemController.insertIntoFavoriteItem(insertFavoriteItemDTOInput));

    assertNotNull(ex.getMessage());
    verify(favoriteItemInConverter, never()).toDto(any(FavoriteItem.class));
  }

  @Test
  @DisplayName("Test controlador getFavoriteItemById Ok")
  void getFavoriteItemByIdOkTest() {

    // INPUT
    Integer userIdInput = 1;
    Integer itemIdInput = 10;

    // ESPERADO
    FavoriteItemDTO dtoEsperado = crearFavoriteItemDTO(1, 10);

    ResponseEntity<FavoriteItemDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    FavoriteItem favoriteItemMock = crearFavoriteItem(1, 10);

    when(selectFavoriteItemByIdUseCase.selectFavoriteItemById(userIdInput, itemIdInput))
        .thenReturn(favoriteItemMock);
    when(favoriteItemInConverter.toDto(favoriteItemMock)).thenReturn(crearFavoriteItemDTO(1, 10));

    // EJECUCIÓN
    ResponseEntity<FavoriteItemDTO> res =
        favoriteItemController.getFavoriteItemById(userIdInput, itemIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.OK, res.getStatusCode());
          assertEquals(dtoEsperado, res.getBody());
          assertEquals(resEsperado, res);
        });
  }

  @Test
  @DisplayName("Test controlador getFavoriteItemById Ko")
  void getFavoriteItemByIdKoTest() {

    // INPUT
    Integer userIdInput = 99;
    Integer itemIdInput = 98;

    // MOCKS
    when(selectFavoriteItemByIdUseCase.selectFavoriteItemById(userIdInput, itemIdInput))
        .thenThrow(new FavoriteItemNotFoundException(99, 98));

    // EJECUCIÓN Y VALIDACIONES
    FavoriteItemNotFoundException ex =
        assertThrows(
            FavoriteItemNotFoundException.class,
            () -> favoriteItemController.getFavoriteItemById(userIdInput, itemIdInput));

    assertEquals(
        "No se encuentra el favorito de artículo con userId: 99 e itemId: 98", ex.getMessage());
    verify(favoriteItemInConverter, never()).toDto(any(FavoriteItem.class));
  }

  @Test
  @DisplayName("Test controlador deleteFavoriteItem Ok")
  void deleteFavoriteItemOkTest() {

    // INPUT
    Integer userIdInput = 1;
    Integer itemIdInput = 10;

    // ESPERADO
    ResponseEntity<Void> resEsperado = ResponseEntity.noContent().build();

    // MOCKS
    doNothing().when(deleteFavoriteItemUseCase).deleteFavoriteItem(userIdInput, itemIdInput);

    // EJECUCIÓN
    ResponseEntity<Void> res = favoriteItemController.deleteFavoriteItem(userIdInput, itemIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
          assertNull(res.getBody());
          assertEquals(resEsperado, res);
        });
    verify(deleteFavoriteItemUseCase).deleteFavoriteItem(userIdInput, itemIdInput);
  }

  @Test
  @DisplayName("Test controlador deleteFavoriteItem Ko")
  void deleteFavoriteItemKoTest() {

    // INPUT
    Integer userIdInput = 99;
    Integer itemIdInput = 98;

    // MOCKS
    doThrow(new FavoriteItemNotFoundException(99, 98))
        .when(deleteFavoriteItemUseCase)
        .deleteFavoriteItem(userIdInput, itemIdInput);

    // EJECUCIÓN Y VALIDACIONES
    FavoriteItemNotFoundException ex =
        assertThrows(
            FavoriteItemNotFoundException.class,
            () -> favoriteItemController.deleteFavoriteItem(userIdInput, itemIdInput));

    assertEquals(
        "No se encuentra el favorito de artículo con userId: 99 e itemId: 98", ex.getMessage());
  }
}
