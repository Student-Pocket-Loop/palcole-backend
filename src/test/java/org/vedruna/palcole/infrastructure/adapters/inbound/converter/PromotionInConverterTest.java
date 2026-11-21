package org.vedruna.palcole.infrastructure.adapters.inbound.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vedruna.palcole.domain.model.Promotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.promotions.InsertPromotionDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.promotions.PromotionDTO;

class PromotionInConverterTest {

  private final PromotionInConverter converter = Mappers.getMapper(PromotionInConverter.class);

  private Promotion crearPromotion(Integer promotionId, String title) {
    Promotion o = new Promotion();
    o.setPromotionId(promotionId);
    o.setCategoryId(1);
    o.setTitle(title);
    o.setDescription("Descuento por la vuelta al cole");
    o.setOriginalPrice(new BigDecimal("20.00"));
    o.setDiscountPrice(new BigDecimal("15.00"));
    o.setDiscountPercentage(25);
    o.setPromoCode("VUELTA25");
    o.setStartDate(LocalDate.of(2026, 9, 1));
    o.setEndDate(LocalDate.of(2026, 9, 30));
    return o;
  }

  private PromotionDTO crearPromotionDTO(Integer promotionId, String title) {
    PromotionDTO o = new PromotionDTO();
    o.setPromotionId(promotionId);
    o.setCategoryId(1);
    o.setTitle(title);
    o.setDescription("Descuento por la vuelta al cole");
    o.setOriginalPrice(new BigDecimal("20.00"));
    o.setDiscountPrice(new BigDecimal("15.00"));
    o.setDiscountPercentage(25);
    o.setPromoCode("VUELTA25");
    o.setStartDate(LocalDate.of(2026, 9, 1));
    o.setEndDate(LocalDate.of(2026, 9, 30));
    return o;
  }

  private InsertPromotionDTO crearInsertPromotionDTO(String title) {
    InsertPromotionDTO o = new InsertPromotionDTO();
    o.setCategoryId(1);
    o.setTitle(title);
    o.setDescription("Descuento por la vuelta al cole");
    o.setOriginalPrice(new BigDecimal("20.00"));
    o.setDiscountPrice(new BigDecimal("15.00"));
    o.setDiscountPercentage(25);
    o.setPromoCode("VUELTA25");
    o.setStartDate(LocalDate.of(2026, 9, 1));
    o.setEndDate(LocalDate.of(2026, 9, 30));
    return o;
  }

  @Test
  @DisplayName("Test converter inbound toDto Ok")
  void toDtoOkTest() {

    // INPUT
    Promotion promotionInput = crearPromotion(1, "Papelería");

    // ESPERADO
    PromotionDTO dtoEsperado = crearPromotionDTO(1, "Papelería");

    // EJECUCIÓN
    PromotionDTO res = converter.toDto(promotionInput);

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
    Promotion promotionInput = null;

    // EJECUCIÓN
    PromotionDTO res = converter.toDto(promotionInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter inbound toDomainInsert Ok")
  void toDomainInsertOkTest() {

    // INPUT
    InsertPromotionDTO dtoInput = crearInsertPromotionDTO("Papelería");

    // EJECUCIÓN
    Promotion res = converter.toDomainInsert(dtoInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertNull(res.getPromotionId());
          assertEquals(dtoInput.getCategoryId(), res.getCategoryId());
          assertEquals(dtoInput.getTitle(), res.getTitle());
          assertEquals(dtoInput.getDescription(), res.getDescription());
          assertEquals(dtoInput.getOriginalPrice(), res.getOriginalPrice());
          assertEquals(dtoInput.getDiscountPrice(), res.getDiscountPrice());
          assertEquals(dtoInput.getDiscountPercentage(), res.getDiscountPercentage());
          assertEquals(dtoInput.getPromoCode(), res.getPromoCode());
          assertEquals(dtoInput.getStartDate(), res.getStartDate());
          assertEquals(dtoInput.getEndDate(), res.getEndDate());
        });
  }

  @Test
  @DisplayName("Test converter inbound toDomainInsert Null")
  void toDomainInsertNullTest() {

    // INPUT
    InsertPromotionDTO dtoInput = null;

    // EJECUCIÓN
    Promotion res = converter.toDomainInsert(dtoInput);

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
    Pageable pageableInput = PageRequest.of(0, 10, Sort.by("promotionId").ascending());

    // EJECUCIÓN
    PageableModel res = converter.toPageableModel(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertEquals(0, res.getPage());
          assertEquals(10, res.getSize());
          assertEquals("promotionId: ASC", res.getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toPromotionsPage Ok (sin ordenar)")
  void toPromotionsPageUnsortedOkTest() {

    // INPUT
    PageModel<Promotion> pageModelInput =
        new PageModel<>(
            List.of(crearPromotion(1, "Papelería"), crearPromotion(2, "Mochilas")), 2, 1, 2, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    Page<PromotionDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearPromotionDTO(1, "Papelería"), crearPromotionDTO(2, "Mochilas")),
            PageRequest.of(0, 10),
            2);

    // EJECUCIÓN
    Page<PromotionDTO> res = converter.toPromotionsPage(pageModelInput, pageableModelInput);

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
  @DisplayName("Test converter inbound toPromotionsPage Ok (orden ascendente)")
  void toPromotionsPageAscOkTest() {

    // INPUT
    PageModel<Promotion> pageModelInput =
        new PageModel<>(List.of(crearPromotion(1, "Papelería")), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "promotionId: ASC");

    // ESPERADO
    Page<PromotionDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearPromotionDTO(1, "Papelería")),
            PageRequest.of(0, 10, Sort.by("promotionId").ascending()),
            1);

    // EJECUCIÓN
    Page<PromotionDTO> res = converter.toPromotionsPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("promotionId").ascending(), res.getPageable().getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toPromotionsPage Ok (orden descendente)")
  void toPromotionsPageDescOkTest() {

    // INPUT
    PageModel<Promotion> pageModelInput =
        new PageModel<>(List.of(crearPromotion(1, "Papelería")), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "promotionId: DESC");

    // ESPERADO
    Page<PromotionDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearPromotionDTO(1, "Papelería")),
            PageRequest.of(0, 10, Sort.by("promotionId").descending()),
            1);

    // EJECUCIÓN
    Page<PromotionDTO> res = converter.toPromotionsPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("promotionId").descending(), res.getPageable().getSort());
        });
  }
}
