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
import org.vedruna.palcole.domain.exception.favoritePromotions.FavoritePromotionAlreadyExistsException;
import org.vedruna.palcole.domain.exception.favoritePromotions.FavoritePromotionNotFoundException;
import org.vedruna.palcole.domain.model.FavoritePromotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.FavoritePromotionOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.FavoritePromotionId;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.FavoritePromotionEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.FavoritePromotionRepository;


@ExtendWith(MockitoExtension.class)
class FavoritePromotionStorageImplTest {

  @Mock private FavoritePromotionRepository favoritePromotionRepository;

  @Mock private FavoritePromotionOutConverter favoritePromotionOutConverter;

  @InjectMocks private FavoritePromotionStorageImpl favoritePromotionStorageImpl;

  private FavoritePromotion crearFavoritePromotion(Integer userId, Integer promotionId) {
    FavoritePromotion o = new FavoritePromotion();
    o.setUserId(userId);
    o.setPromotionId(promotionId);
    return o;
  }

  private FavoritePromotionEntity crearFavoritePromotionEntity(
      Integer userId, Integer promotionId) {
    FavoritePromotionEntity o = new FavoritePromotionEntity();
    o.setUserId(userId);
    o.setPromotionId(promotionId);
    return o;
  }

  @Test
  @DisplayName("Test storage selectAllFavoritePromotions Ok")
  void selectAllFavoritePromotionsOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<FavoritePromotion> pageModelEsperado =
        new PageModel<>(
            List.of(crearFavoritePromotion(1, 10), crearFavoritePromotion(2, 20)), 2, 1, 2, 10, 0);

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);
    Page<FavoritePromotionEntity> pageEntityMock =
        new PageImpl<>(
            List.of(crearFavoritePromotionEntity(1, 10), crearFavoritePromotionEntity(2, 20)),
            pageableMock,
            2);
    PageModel<FavoritePromotion> pageModelMock =
        new PageModel<>(
            List.of(crearFavoritePromotion(1, 10), crearFavoritePromotion(2, 20)), 2, 1, 2, 10, 0);

    when(favoritePromotionOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(favoritePromotionRepository.findAll(pageableMock)).thenReturn(pageEntityMock);
    when(favoritePromotionOutConverter.toFavoritePromotionPageModel(pageEntityMock))
        .thenReturn(pageModelMock);

    // EJECUCIÓN
    PageModel<FavoritePromotion> res =
        favoritePromotionStorageImpl.selectAllFavoritePromotions(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage selectAllFavoritePromotions Ko")
  void selectAllFavoritePromotionsKoTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "INVALID_SORT");

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);

    when(favoritePromotionOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(favoritePromotionRepository.findAll(pageableMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> favoritePromotionStorageImpl.selectAllFavoritePromotions(pageableModelInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(favoritePromotionOutConverter, never()).toFavoritePromotionPageModel(any());
  }

  @Test
  @DisplayName("Test storage findFavoritePromotionById Ok")
  void findFavoritePromotionByIdOkTest() {

    // INPUT
    Integer userIdInput = 1;
    Integer promotionIdInput = 10;

    // ESPERADO
    FavoritePromotion favoritePromotionEsperado = crearFavoritePromotion(1, 10);

    // MOCKS
    FavoritePromotionEntity favoritePromotionEntityMock = crearFavoritePromotionEntity(1, 10);
    FavoritePromotion favoritePromotionMock = crearFavoritePromotion(1, 10);

    when(favoritePromotionRepository.findById(
            new FavoritePromotionId(userIdInput, promotionIdInput)))
        .thenReturn(Optional.of(favoritePromotionEntityMock));
    when(favoritePromotionOutConverter.toDomain(favoritePromotionEntityMock))
        .thenReturn(favoritePromotionMock);

    // EJECUCIÓN
    FavoritePromotion res =
        favoritePromotionStorageImpl.findFavoritePromotionById(userIdInput, promotionIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(favoritePromotionEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage findFavoritePromotionById Ko")
  void findFavoritePromotionByIdKoTest() {

    // INPUT
    Integer userIdInput = 99;
    Integer promotionIdInput = 98;

    // MOCKS
    when(favoritePromotionRepository.findById(
            new FavoritePromotionId(userIdInput, promotionIdInput)))
        .thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    FavoritePromotionNotFoundException ex =
        assertThrows(
            FavoritePromotionNotFoundException.class,
            () ->
                favoritePromotionStorageImpl.findFavoritePromotionById(
                    userIdInput, promotionIdInput));

    assertEquals(
        "No se encuentra el favorito de promoción con userId: 99 e promotionId: 98",
        ex.getMessage());
    verify(favoritePromotionOutConverter, never()).toDomain(any(FavoritePromotionEntity.class));
  }

  @Test
  @DisplayName("Test storage insertFavoritePromotion Ok")
  void insertFavoritePromotionOkTest() {

    // INPUT
    FavoritePromotion newFavoritePromotionInput = crearFavoritePromotion(1, 10);

    // ESPERADO
    FavoritePromotion favoritePromotionEsperado = crearFavoritePromotion(1, 10);

    // MOCKS
    FavoritePromotionEntity favoritePromotionEntityNuevoMock = crearFavoritePromotionEntity(1, 10);
    FavoritePromotionEntity favoritePromotionEntityGuardadoMock =
        crearFavoritePromotionEntity(1, 10);
    FavoritePromotion favoritePromotionCreadoMock = crearFavoritePromotion(1, 10);

    when(favoritePromotionRepository.existsById(new FavoritePromotionId(1, 10))).thenReturn(false);
    when(favoritePromotionOutConverter.toEntity(newFavoritePromotionInput))
        .thenReturn(favoritePromotionEntityNuevoMock);
    when(favoritePromotionRepository.save(favoritePromotionEntityNuevoMock))
        .thenReturn(favoritePromotionEntityGuardadoMock);
    when(favoritePromotionOutConverter.toDomain(favoritePromotionEntityGuardadoMock))
        .thenReturn(favoritePromotionCreadoMock);

    // EJECUCIÓN
    FavoritePromotion res =
        favoritePromotionStorageImpl.insertFavoritePromotion(newFavoritePromotionInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(favoritePromotionEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage insertFavoritePromotion Ko")
  void insertFavoritePromotionKoTest() {

    // INPUT
    FavoritePromotion newFavoritePromotionInput = crearFavoritePromotion(1, 10);

    // MOCKS
    when(favoritePromotionRepository.existsById(new FavoritePromotionId(1, 10))).thenReturn(true);

    // EJECUCIÓN Y VALIDACIONES
    FavoritePromotionAlreadyExistsException ex =
        assertThrows(
            FavoritePromotionAlreadyExistsException.class,
            () -> favoritePromotionStorageImpl.insertFavoritePromotion(newFavoritePromotionInput));

    assertTrue(ex.getMessage().startsWith("Ya existe el favorito de promoción"));
    verify(favoritePromotionRepository, never()).save(any(FavoritePromotionEntity.class));
    verify(favoritePromotionOutConverter, never()).toDomain(any(FavoritePromotionEntity.class));
  }

  @Test
  @DisplayName("Test storage deleteFavoritePromotion Ok")
  void deleteFavoritePromotionOkTest() {

    // INPUT
    Integer userIdInput = 1;
    Integer promotionIdInput = 10;

    // MOCKS
    when(favoritePromotionRepository.existsById(
            new FavoritePromotionId(userIdInput, promotionIdInput)))
        .thenReturn(true);
    doNothing()
        .when(favoritePromotionRepository)
        .deleteById(new FavoritePromotionId(userIdInput, promotionIdInput));

    // EJECUCIÓN
    favoritePromotionStorageImpl.deleteFavoritePromotion(userIdInput, promotionIdInput);

    // VALIDACIONES
    verify(favoritePromotionRepository)
        .existsById(new FavoritePromotionId(userIdInput, promotionIdInput));
    verify(favoritePromotionRepository)
        .deleteById(new FavoritePromotionId(userIdInput, promotionIdInput));
  }

  @Test
  @DisplayName("Test storage deleteFavoritePromotion Ko")
  void deleteFavoritePromotionKoTest() {

    // INPUT
    Integer userIdInput = 99;
    Integer promotionIdInput = 98;

    // MOCKS
    when(favoritePromotionRepository.existsById(
            new FavoritePromotionId(userIdInput, promotionIdInput)))
        .thenReturn(false);

    // EJECUCIÓN Y VALIDACIONES
    FavoritePromotionNotFoundException ex =
        assertThrows(
            FavoritePromotionNotFoundException.class,
            () ->
                favoritePromotionStorageImpl.deleteFavoritePromotion(
                    userIdInput, promotionIdInput));

    assertEquals(
        "No se encuentra el favorito de promoción con userId: 99 e promotionId: 98",
        ex.getMessage());
    verify(favoritePromotionRepository, never()).deleteById(any());
  }
}
