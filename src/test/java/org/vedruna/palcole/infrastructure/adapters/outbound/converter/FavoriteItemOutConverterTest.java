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
import org.vedruna.palcole.domain.model.FavoriteItem;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.FavoriteItemEntity;

class FavoriteItemOutConverterTest {

  private final FavoriteItemOutConverter favoriteItemOutConverter =
      Mappers.getMapper(FavoriteItemOutConverter.class);

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
  @DisplayName("Test converter toDomain Ok")
  void toDomainOkTest() {

    // INPUT
    FavoriteItemEntity favoriteItemEntityInput = crearFavoriteItemEntity(1, 10);

    // ESPERADO
    FavoriteItem favoriteItemEsperado = crearFavoriteItem(1, 10);

    // EJECUCIÓN
    FavoriteItem res = favoriteItemOutConverter.toDomain(favoriteItemEntityInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(favoriteItemEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toDomain Null")
  void toDomainNullTest() {

    // INPUT
    FavoriteItemEntity favoriteItemEntityInput = null;

    // EJECUCIÓN
    FavoriteItem res = favoriteItemOutConverter.toDomain(favoriteItemEntityInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter toEntity Ok")
  void toEntityOkTest() {

    // INPUT
    FavoriteItem favoriteItemInput = crearFavoriteItem(1, 10);

    // ESPERADO
    FavoriteItemEntity favoriteItemEntityEsperado = crearFavoriteItemEntity(1, 10);

    // EJECUCIÓN
    FavoriteItemEntity res = favoriteItemOutConverter.toEntity(favoriteItemInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(favoriteItemEntityEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toEntity Null")
  void toEntityNullTest() {

    // INPUT
    FavoriteItem favoriteItemInput = null;

    // EJECUCIÓN
    FavoriteItemEntity res = favoriteItemOutConverter.toEntity(favoriteItemInput);

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
    Pageable res = favoriteItemOutConverter.toPageable(pageableModelInput);

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
    Pageable res = favoriteItemOutConverter.toPageable(pageableModelInput);

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
    Pageable res = favoriteItemOutConverter.toPageable(pageableModelInput);

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
        () -> favoriteItemOutConverter.toPageable(pageableModelInput));
  }

  @Test
  @DisplayName("Test converter toFavoriteItemPageModel Ok")
  void toFavoriteItemPageModelOkTest() {

    // INPUT
    Pageable pageable = PageRequest.of(0, 10);
    Page<FavoriteItemEntity> pageInput =
        new PageImpl<>(
            List.of(crearFavoriteItemEntity(1, 10), crearFavoriteItemEntity(2, 20)), pageable, 2);

    // ESPERADO
    PageModel<FavoriteItem> pageModelEsperado =
        new PageModel<>(
            List.of(crearFavoriteItem(1, 10), crearFavoriteItem(2, 20)), 2, 1, 2, 10, 0);

    // EJECUCIÓN
    PageModel<FavoriteItem> res = favoriteItemOutConverter.toFavoriteItemPageModel(pageInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toFavoriteItemPageModel Ko")
  void toFavoriteItemPageModelKoTest() {

    // INPUT
    Page<FavoriteItemEntity> pageInput = null;

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(
        NullPointerException.class,
        () -> favoriteItemOutConverter.toFavoriteItemPageModel(pageInput));
  }
}
