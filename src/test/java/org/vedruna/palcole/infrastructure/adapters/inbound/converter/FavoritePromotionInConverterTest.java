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
import org.vedruna.palcole.domain.model.FavoritePromotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.favoritePromotions.FavoritePromotionDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.favoritePromotions.InsertFavoritePromotionDTO;

class FavoritePromotionInConverterTest {

  private final FavoritePromotionInConverter converter =
      Mappers.getMapper(FavoritePromotionInConverter.class);

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
  @DisplayName("Test converter inbound toDto Ok")
  void toDtoOkTest() {

    // INPUT
    FavoritePromotion favoritePromotionInput = crearFavoritePromotion(1, 10);

    // ESPERADO
    FavoritePromotionDTO dtoEsperado = crearFavoritePromotionDTO(1, 10);

    // EJECUCIÓN
    FavoritePromotionDTO res = converter.toDto(favoritePromotionInput);

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
    FavoritePromotion favoritePromotionInput = null;

    // EJECUCIÓN
    FavoritePromotionDTO res = converter.toDto(favoritePromotionInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter inbound toDomainInsert Ok")
  void toDomainInsertOkTest() {

    // INPUT
    InsertFavoritePromotionDTO dtoInput = crearInsertFavoritePromotionDTO(1, 10);

    // EJECUCIÓN
    FavoritePromotion res = converter.toDomainInsert(dtoInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(dtoInput.getUserId(), res.getUserId());
          assertEquals(dtoInput.getPromotionId(), res.getPromotionId());
        });
  }

  @Test
  @DisplayName("Test converter inbound toDomainInsert Null")
  void toDomainInsertNullTest() {

    // INPUT
    InsertFavoritePromotionDTO dtoInput = null;

    // EJECUCIÓN
    FavoritePromotion res = converter.toDomainInsert(dtoInput);

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
  @DisplayName("Test converter inbound toFavoritePromotionsPage Ok (sin ordenar)")
  void toFavoritePromotionsPageUnsortedOkTest() {

    // INPUT
    PageModel<FavoritePromotion> pageModelInput =
        new PageModel<>(
            List.of(crearFavoritePromotion(1, 10), crearFavoritePromotion(2, 20)), 2, 1, 2, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    Page<FavoritePromotionDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearFavoritePromotionDTO(1, 10), crearFavoritePromotionDTO(2, 20)),
            PageRequest.of(0, 10),
            2);

    // EJECUCIÓN
    Page<FavoritePromotionDTO> res =
        converter.toFavoritePromotionsPage(pageModelInput, pageableModelInput);

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
  @DisplayName("Test converter inbound toFavoritePromotionsPage Ok (orden ascendente)")
  void toFavoritePromotionsPageAscOkTest() {

    // INPUT
    PageModel<FavoritePromotion> pageModelInput =
        new PageModel<>(List.of(crearFavoritePromotion(1, 10)), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "userId: ASC");

    // ESPERADO
    Page<FavoritePromotionDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearFavoritePromotionDTO(1, 10)),
            PageRequest.of(0, 10, Sort.by("userId").ascending()),
            1);

    // EJECUCIÓN
    Page<FavoritePromotionDTO> res =
        converter.toFavoritePromotionsPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("userId").ascending(), res.getPageable().getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toFavoritePromotionsPage Ok (orden descendente)")
  void toFavoritePromotionsPageDescOkTest() {

    // INPUT
    PageModel<FavoritePromotion> pageModelInput =
        new PageModel<>(List.of(crearFavoritePromotion(1, 10)), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "userId: DESC");

    // ESPERADO
    Page<FavoritePromotionDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearFavoritePromotionDTO(1, 10)),
            PageRequest.of(0, 10, Sort.by("userId").descending()),
            1);

    // EJECUCIÓN
    Page<FavoritePromotionDTO> res =
        converter.toFavoritePromotionsPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("userId").descending(), res.getPageable().getSort());
        });
  }
}
