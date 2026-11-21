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
import org.vedruna.palcole.domain.exception.favoritePromotions.FavoritePromotionAlreadyExistsException;
import org.vedruna.palcole.domain.exception.favoritePromotions.FavoritePromotionNotFoundException;
import org.vedruna.palcole.domain.model.FavoritePromotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.DeleteFavoritePromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.NewFavoritePromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.SelectAllFavoritePromotionsUseCase;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.SelectFavoritePromotionByIdUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.FavoritePromotionInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.favoritePromotions.FavoritePromotionDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.favoritePromotions.InsertFavoritePromotionDTO;

@ExtendWith(MockitoExtension.class)
class FavoritePromotionControllerTest {

  @Mock private NewFavoritePromotionUseCase newFavoritePromotionUseCase;

  @Mock private DeleteFavoritePromotionUseCase deleteFavoritePromotionUseCase;

  @Mock private SelectAllFavoritePromotionsUseCase selectAllFavoritePromotionsUseCase;

  @Mock private SelectFavoritePromotionByIdUseCase selectFavoritePromotionByIdUseCase;

  @Mock private FavoritePromotionInConverter favoritePromotionInConverter;

  @InjectMocks private FavoritePromotionController favoritePromotionController;

  private FavoritePromotion crearFavoritePromotion(Integer userId, Integer promotionId) {
    FavoritePromotion o = new FavoritePromotion();
    o.setUserId(userId);
    o.setPromotionId(promotionId);
    return o;
  }

  private FavoritePromotionDTO crearFavoritePromotionDTO(Integer userId, Integer promotionId) {
    FavoritePromotionDTO o = new FavoritePromotionDTO();
    o.setUserId(userId);
    o.setPromotionId(promotionId);
    return o;
  }

  private InsertFavoritePromotionDTO crearInsertFavoritePromotionDTO(
      Integer userId, Integer promotionId) {
    InsertFavoritePromotionDTO o = new InsertFavoritePromotionDTO();
    o.setUserId(userId);
    o.setPromotionId(promotionId);
    return o;
  }

  @Test
  @DisplayName("Test controlador getAll Ok")
  void getAllOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10);

    // ESPERADO
    List<FavoritePromotionDTO> lista =
        List.of(crearFavoritePromotionDTO(1, 10), crearFavoritePromotionDTO(2, 20));

    Pageable pageableEsperado = PageRequest.of(0, 10);

    Page<FavoritePromotionDTO> pageEsperado = new PageImpl<>(lista, pageableEsperado, 2);

    ResponseEntity<Page<FavoritePromotionDTO>> resEsperado = ResponseEntity.ok(pageEsperado);

    // MOCKS
    // CONVERTER MOCK (Pageable -> PageableModel)
    PageableModel pageableModelMock = new PageableModel(0, 10, "UNSORTED");

    when(favoritePromotionInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);

    // USE CASE MOCK
    PageModel<FavoritePromotion> pageModelMock =
        new PageModel<>(
            List.of(crearFavoritePromotion(1, 10), crearFavoritePromotion(2, 20)), 2, 1, 2, 10, 0);

    when(selectAllFavoritePromotionsUseCase.selectAllFavoritePromotions(pageableModelMock))
        .thenReturn(pageModelMock);

    // CONVERTER MOCK (PageModel -> Page de DTOs)
    Page<FavoritePromotionDTO> pageDtoMock =
        new PageImpl<>(
            List.of(crearFavoritePromotionDTO(1, 10), crearFavoritePromotionDTO(2, 20)),
            PageRequest.of(0, 10),
            2);

    when(favoritePromotionInConverter.toFavoritePromotionsPage(pageModelMock, pageableModelMock))
        .thenReturn(pageDtoMock);

    // EJECUCIÓN
    ResponseEntity<Page<FavoritePromotionDTO>> res =
        favoritePromotionController.getAllFavoritePromotions(pageableInput);

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

    when(favoritePromotionInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);
    when(selectAllFavoritePromotionsUseCase.selectAllFavoritePromotions(pageableModelMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> favoritePromotionController.getAllFavoritePromotions(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(favoritePromotionInConverter, never()).toFavoritePromotionsPage(any(), any());
  }

  @Test
  @DisplayName("Test controlador insertIntoFavoritePromotion Ok")
  void insertIntoFavoritePromotionOkTest() {

    // INPUT
    InsertFavoritePromotionDTO insertFavoritePromotionDTOInput =
        crearInsertFavoritePromotionDTO(1, 10);

    // ESPERADO
    FavoritePromotionDTO dtoEsperado = crearFavoritePromotionDTO(1, 10);

    ResponseEntity<FavoritePromotionDTO> resEsperado =
        ResponseEntity.status(HttpStatus.CREATED).body(dtoEsperado);

    // MOCKS
    FavoritePromotion favoritePromotionNuevoMock = crearFavoritePromotion(1, 10);
    FavoritePromotion favoritePromotionCreadoMock = crearFavoritePromotion(1, 10);

    when(favoritePromotionInConverter.toDomainInsert(insertFavoritePromotionDTOInput))
        .thenReturn(favoritePromotionNuevoMock);
    when(newFavoritePromotionUseCase.newFavoritePromotion(favoritePromotionNuevoMock))
        .thenReturn(favoritePromotionCreadoMock);
    when(favoritePromotionInConverter.toDto(favoritePromotionCreadoMock))
        .thenReturn(crearFavoritePromotionDTO(1, 10));

    // EJECUCIÓN
    ResponseEntity<FavoritePromotionDTO> res =
        favoritePromotionController.insertIntoFavoritePromotion(insertFavoritePromotionDTOInput);

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
  @DisplayName("Test controlador insertIntoFavoritePromotion Ko")
  void insertIntoFavoritePromotionKoTest() {

    // INPUT
    InsertFavoritePromotionDTO insertFavoritePromotionDTOInput =
        crearInsertFavoritePromotionDTO(1, 10);

    // MOCKS
    FavoritePromotion favoritePromotionNuevoMock = crearFavoritePromotion(1, 10);

    when(favoritePromotionInConverter.toDomainInsert(insertFavoritePromotionDTOInput))
        .thenReturn(favoritePromotionNuevoMock);
    when(newFavoritePromotionUseCase.newFavoritePromotion(favoritePromotionNuevoMock))
        .thenThrow(new FavoritePromotionAlreadyExistsException(1, 10));

    // EJECUCIÓN Y VALIDACIONES
    FavoritePromotionAlreadyExistsException ex =
        assertThrows(
            FavoritePromotionAlreadyExistsException.class,
            () ->
                favoritePromotionController.insertIntoFavoritePromotion(
                    insertFavoritePromotionDTOInput));

    assertNotNull(ex.getMessage());
    verify(favoritePromotionInConverter, never()).toDto(any(FavoritePromotion.class));
  }

  @Test
  @DisplayName("Test controlador getFavoritePromotionById Ok")
  void getFavoritePromotionByIdOkTest() {

    // INPUT
    Integer userIdInput = 1;
    Integer promotionIdInput = 10;

    // ESPERADO
    FavoritePromotionDTO dtoEsperado = crearFavoritePromotionDTO(1, 10);

    ResponseEntity<FavoritePromotionDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    FavoritePromotion favoritePromotionMock = crearFavoritePromotion(1, 10);

    when(selectFavoritePromotionByIdUseCase.selectFavoritePromotionById(
            userIdInput, promotionIdInput))
        .thenReturn(favoritePromotionMock);
    when(favoritePromotionInConverter.toDto(favoritePromotionMock))
        .thenReturn(crearFavoritePromotionDTO(1, 10));

    // EJECUCIÓN
    ResponseEntity<FavoritePromotionDTO> res =
        favoritePromotionController.getFavoritePromotionById(userIdInput, promotionIdInput);

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
  @DisplayName("Test controlador getFavoritePromotionById Ko")
  void getFavoritePromotionByIdKoTest() {

    // INPUT
    Integer userIdInput = 99;
    Integer promotionIdInput = 98;

    // MOCKS
    when(selectFavoritePromotionByIdUseCase.selectFavoritePromotionById(
            userIdInput, promotionIdInput))
        .thenThrow(new FavoritePromotionNotFoundException(99, 98));

    // EJECUCIÓN Y VALIDACIONES
    FavoritePromotionNotFoundException ex =
        assertThrows(
            FavoritePromotionNotFoundException.class,
            () ->
                favoritePromotionController.getFavoritePromotionById(
                    userIdInput, promotionIdInput));

    assertEquals(
        "No se encuentra el favorito de promoción con userId: 99 e promotionId: 98",
        ex.getMessage());
    verify(favoritePromotionInConverter, never()).toDto(any(FavoritePromotion.class));
  }

  @Test
  @DisplayName("Test controlador deleteFavoritePromotion Ok")
  void deleteFavoritePromotionOkTest() {

    // INPUT
    Integer userIdInput = 1;
    Integer promotionIdInput = 10;

    // ESPERADO
    ResponseEntity<Void> resEsperado = ResponseEntity.noContent().build();

    // MOCKS
    doNothing()
        .when(deleteFavoritePromotionUseCase)
        .deleteFavoritePromotion(userIdInput, promotionIdInput);

    // EJECUCIÓN
    ResponseEntity<Void> res =
        favoritePromotionController.deleteFavoritePromotion(userIdInput, promotionIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
          assertNull(res.getBody());
          assertEquals(resEsperado, res);
        });
    verify(deleteFavoritePromotionUseCase).deleteFavoritePromotion(userIdInput, promotionIdInput);
  }

  @Test
  @DisplayName("Test controlador deleteFavoritePromotion Ko")
  void deleteFavoritePromotionKoTest() {

    // INPUT
    Integer userIdInput = 99;
    Integer promotionIdInput = 98;

    // MOCKS
    doThrow(new FavoritePromotionNotFoundException(99, 98))
        .when(deleteFavoritePromotionUseCase)
        .deleteFavoritePromotion(userIdInput, promotionIdInput);

    // EJECUCIÓN Y VALIDACIONES
    FavoritePromotionNotFoundException ex =
        assertThrows(
            FavoritePromotionNotFoundException.class,
            () ->
                favoritePromotionController.deleteFavoritePromotion(userIdInput, promotionIdInput));

    assertEquals(
        "No se encuentra el favorito de promoción con userId: 99 e promotionId: 98",
        ex.getMessage());
  }
}
