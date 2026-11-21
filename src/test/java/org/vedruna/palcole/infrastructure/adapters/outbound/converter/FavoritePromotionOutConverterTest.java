package org.vedruna.palcole.infrastructure.adapters.outbound.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vedruna.palcole.domain.model.FavoritePromotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.FavoritePromotionEntity;

class FavoritePromotionOutConverterTest {

  private final FavoritePromotionOutConverter favoritePromotionOutConverter =
      Mappers.getMapper(FavoritePromotionOutConverter.class);

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
  @DisplayName("Test converter toDomain Ok")
  void toDomainOkTest() {

    // INPUT
    FavoritePromotionEntity favoritePromotionEntityInput = crearFavoritePromotionEntity(1, 10);

    // ESPERADO
    FavoritePromotion favoritePromotionEsperado = crearFavoritePromotion(1, 10);

    // EJECUCIÓN
    FavoritePromotion res = favoritePromotionOutConverter.toDomain(favoritePromotionEntityInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(favoritePromotionEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toDomain Null")
  void toDomainNullTest() {

    // INPUT
    FavoritePromotionEntity favoritePromotionEntityInput = null;

    // EJECUCIÓN
    FavoritePromotion res = favoritePromotionOutConverter.toDomain(favoritePromotionEntityInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter toEntity Ok")
  void toEntityOkTest() {

    // INPUT
    FavoritePromotion favoritePromotionInput = crearFavoritePromotion(1, 10);

    // ESPERADO
    FavoritePromotionEntity favoritePromotionEntityEsperado = crearFavoritePromotionEntity(1, 10);

    // EJECUCIÓN
    FavoritePromotionEntity res = favoritePromotionOutConverter.toEntity(favoritePromotionInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(favoritePromotionEntityEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toEntity Null")
  void toEntityNullTest() {

    // INPUT
    FavoritePromotion favoritePromotionInput = null;

    // EJECUCIÓN
    FavoritePromotionEntity res = favoritePromotionOutConverter.toEntity(favoritePromotionInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter toPageable Unsorted Ok")
  void toPageableUnsortedOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10);

    // EJECUCIÓN
    Pageable res = favoritePromotionOutConverter.toPageable(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageableEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toPageable Sorted ASC Ok")
  void toPageableSortedAscOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "userId: ASC");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by("userId").ascending());

    // EJECUCIÓN
    Pageable res = favoritePromotionOutConverter.toPageable(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageableEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toPageable Sorted DESC Ok")
  void toPageableSortedDescOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "userId: DESC");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by("userId").descending());

    // EJECUCIÓN
    Pageable res = favoritePromotionOutConverter.toPageable(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageableEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toPageable Ko")
  void toPageableKoTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "FORMATO_INVALIDO");

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(
        ArrayIndexOutOfBoundsException.class,
        () -> favoritePromotionOutConverter.toPageable(pageableModelInput));
  }

  @Test
  @DisplayName("Test converter toFavoritePromotionPageModel Ok")
  void toFavoritePromotionPageModelOkTest() {

    // INPUT
    Pageable pageable = PageRequest.of(0, 10);
    Page<FavoritePromotionEntity> pageInput =
        new PageImpl<>(
            List.of(crearFavoritePromotionEntity(1, 10), crearFavoritePromotionEntity(2, 20)),
            pageable,
            2);

    // ESPERADO
    PageModel<FavoritePromotion> pageModelEsperado =
        new PageModel<>(
            List.of(crearFavoritePromotion(1, 10), crearFavoritePromotion(2, 20)), 2, 1, 2, 10, 0);

    // EJECUCIÓN
    PageModel<FavoritePromotion> res =
        favoritePromotionOutConverter.toFavoritePromotionPageModel(pageInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toFavoritePromotionPageModel Ko")
  void toFavoritePromotionPageModelKoTest() {

    // INPUT
    Page<FavoritePromotionEntity> pageInput = null;

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(
        NullPointerException.class,
        () -> favoritePromotionOutConverter.toFavoritePromotionPageModel(pageInput));
  }
}
