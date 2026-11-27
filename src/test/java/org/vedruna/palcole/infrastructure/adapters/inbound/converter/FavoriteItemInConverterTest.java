package org.vedruna.palcole.infrastructure.adapters.inbound.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.favouriteitem.FavoriteItemDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.favouriteitem.InsertFavoriteItemDTO;

class FavoriteItemInConverterTest {

  private final FavoriteItemInConverter converter =
      Mappers.getMapper(FavoriteItemInConverter.class);

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
  @DisplayName("Test converter inbound toDto Ok")
  void toDtoOkTest() {

    // INPUT
    FavoriteItem favoriteItemInput = crearFavoriteItem(1, 10);

    // ESPERADO
    FavoriteItemDTO dtoEsperado = crearFavoriteItemDTO(1, 10);

    // EJECUCIÓN
    FavoriteItemDTO res = converter.toDto(favoriteItemInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(dtoEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter inbound toDto Null")
  void toDtoNullTest() {

    // INPUT
    FavoriteItem favoriteItemInput = null;

    // EJECUCIÓN
    FavoriteItemDTO res = converter.toDto(favoriteItemInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter inbound toDomainInsert Ok")
  void toDomainInsertOkTest() {

    // INPUT
    InsertFavoriteItemDTO dtoInput = crearInsertFavoriteItemDTO(1, 10);

    // EJECUCIÓN
    FavoriteItem res = converter.toDomainInsert(dtoInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(dtoInput.getUserId(), res.getUserId());
          assertEquals(dtoInput.getItemId(), res.getItemId());
        });
  }

  @Test
  @DisplayName("Test converter inbound toDomainInsert Null")
  void toDomainInsertNullTest() {

    // INPUT
    InsertFavoriteItemDTO dtoInput = null;

    // EJECUCIÓN
    FavoriteItem res = converter.toDomainInsert(dtoInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter inbound toPageableModel Ok (sin ordenar)")
  void toPageableModelUnsortedOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(1, 5);

    // EJECUCIÓN
    PageableModel res = converter.toPageableModel(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertEquals(1, res.getPage());
          assertEquals(5, res.getSize());
          assertEquals("UNSORTED", res.getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toPageableModel Ok (ordenado)")
  void toPageableModelSortedOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10, Sort.by("userId").ascending());

    // EJECUCIÓN
    PageableModel res = converter.toPageableModel(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertEquals(0, res.getPage());
          assertEquals(10, res.getSize());
          assertEquals("userId: ASC", res.getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toFavoriteItemsPage Ok (sin ordenar)")
  void toFavoriteItemsPageUnsortedOkTest() {

    // INPUT
    PageModel<FavoriteItem> pageModelInput =
        new PageModel<>(
            List.of(crearFavoriteItem(1, 10), crearFavoriteItem(2, 20)), 2, 1, 2, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    Page<FavoriteItemDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearFavoriteItemDTO(1, 10), crearFavoriteItemDTO(2, 20)),
            PageRequest.of(0, 10),
            2);

    // EJECUCIÓN
    Page<FavoriteItemDTO> res = converter.toFavoriteItemsPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(2, res.getTotalElements());
          assertEquals(pageEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter inbound toFavoriteItemsPage Ok (orden ascendente)")
  void toFavoriteItemsPageAscOkTest() {

    // INPUT
    PageModel<FavoriteItem> pageModelInput =
        new PageModel<>(List.of(crearFavoriteItem(1, 10)), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "userId: ASC");

    // ESPERADO
    Page<FavoriteItemDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearFavoriteItemDTO(1, 10)),
            PageRequest.of(0, 10, Sort.by("userId").ascending()),
            1);

    // EJECUCIÓN
    Page<FavoriteItemDTO> res = converter.toFavoriteItemsPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("userId").ascending(), res.getPageable().getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toFavoriteItemsPage Ok (orden descendente)")
  void toFavoriteItemsPageDescOkTest() {

    // INPUT
    PageModel<FavoriteItem> pageModelInput =
        new PageModel<>(List.of(crearFavoriteItem(1, 10)), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "userId: DESC");

    // ESPERADO
    Page<FavoriteItemDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearFavoriteItemDTO(1, 10)),
            PageRequest.of(0, 10, Sort.by("userId").descending()),
            1);

    // EJECUCIÓN
    Page<FavoriteItemDTO> res = converter.toFavoriteItemsPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("userId").descending(), res.getPageable().getSort());
        });
  }
}
