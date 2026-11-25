package org.vedruna.palcole.infrastructure.adapters.inbound.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vedruna.palcole.domain.model.AcademicItem;
import org.vedruna.palcole.domain.model.enums.ItemCondition;
import org.vedruna.palcole.domain.model.enums.ListingType;
import org.vedruna.palcole.domain.model.enums.SaleStatus;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems.AcademicItemDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems.InsertAcademicItemDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems.UpdateAcademicItemDTO;

class AcademicItemInConverterTest {

  private final AcademicItemInConverter converter =
      Mappers.getMapper(AcademicItemInConverter.class);

  private AcademicItem crearAcademicItem(Integer itemId, String title) {
    AcademicItem o = new AcademicItem();
    o.setItemId(itemId);
    o.setStudentId(1);
    o.setCategoryId(1);
    o.setTitle(title);
    o.setDescription("En buen estado");
    o.setPrice(new BigDecimal("10.00"));
    o.setItemCondition(ItemCondition.USADO);
    o.setListingType(ListingType.VENTA);
    o.setSaleStatus(SaleStatus.DISPONIBLE);
    o.setPublishedAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    o.setImageUrl("https://ejemplo.com/calculadora.jpg");
    return o;
  }

  private AcademicItemDTO crearAcademicItemDTO(Integer itemId, String title) {
    AcademicItemDTO o = new AcademicItemDTO();
    o.setItemId(itemId);
    o.setStudentId(1);
    o.setCategoryId(1);
    o.setTitle(title);
    o.setDescription("En buen estado");
    o.setPrice(new BigDecimal("10.00"));
    o.setItemCondition(ItemCondition.USADO);
    o.setListingType(ListingType.VENTA);
    o.setSaleStatus(SaleStatus.DISPONIBLE);
    o.setPublishedAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    o.setImageUrl("https://ejemplo.com/calculadora.jpg");
    return o;
  }

  private InsertAcademicItemDTO crearInsertAcademicItemDTO(String title) {
    InsertAcademicItemDTO o = new InsertAcademicItemDTO();
    o.setStudentId(1);
    o.setCategoryId(1);
    o.setTitle(title);
    o.setDescription("En buen estado");
    o.setPrice(new BigDecimal("10.00"));
    o.setItemCondition(ItemCondition.USADO);
    o.setListingType(ListingType.VENTA);
    o.setSaleStatus(SaleStatus.DISPONIBLE);
    o.setImageUrl("https://ejemplo.com/calculadora.jpg");
    return o;
  }

  private UpdateAcademicItemDTO crearUpdateAcademicItemDTO(String title) {
    UpdateAcademicItemDTO o = new UpdateAcademicItemDTO();
    o.setCategoryId(1);
    o.setTitle(title);
    o.setDescription("En buen estado");
    o.setPrice(new BigDecimal("10.00"));
    o.setItemCondition(ItemCondition.USADO);
    o.setListingType(ListingType.VENTA);
    o.setSaleStatus(SaleStatus.DISPONIBLE);
    o.setImageUrl("https://ejemplo.com/calculadora.jpg");
    return o;
  }

  @Test
  @DisplayName("Test converter inbound toDto Ok")
  void toDtoOkTest() {

    // INPUT
    AcademicItem academicItemInput = crearAcademicItem(1, "Calculadora");

    // ESPERADO
    AcademicItemDTO dtoEsperado = crearAcademicItemDTO(1, "Calculadora");

    // EJECUCIÓN
    AcademicItemDTO res = converter.toDto(academicItemInput);

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
    AcademicItem academicItemInput = null;

    // EJECUCIÓN
    AcademicItemDTO res = converter.toDto(academicItemInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter inbound toDomainInsert Ok")
  void toDomainInsertOkTest() {

    // INPUT
    InsertAcademicItemDTO dtoInput = crearInsertAcademicItemDTO("Calculadora");

    // EJECUCIÓN
    AcademicItem res = converter.toDomainInsert(dtoInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertNull(res.getItemId());
          assertEquals(dtoInput.getStudentId(), res.getStudentId());
          assertEquals(dtoInput.getCategoryId(), res.getCategoryId());
          assertEquals(dtoInput.getTitle(), res.getTitle());
          assertEquals(dtoInput.getDescription(), res.getDescription());
          assertEquals(dtoInput.getPrice(), res.getPrice());
          assertEquals(dtoInput.getItemCondition(), res.getItemCondition());
          assertEquals(dtoInput.getListingType(), res.getListingType());
          assertEquals(dtoInput.getSaleStatus(), res.getSaleStatus());
          assertNotNull(res.getPublishedAt());
          assertEquals(dtoInput.getImageUrl(), res.getImageUrl());
        });
  }

  @Test
  @DisplayName("Test converter inbound toDomainInsert Null")
  void toDomainInsertNullTest() {

    // INPUT
    InsertAcademicItemDTO dtoInput = null;

    // EJECUCIÓN
    AcademicItem res = converter.toDomainInsert(dtoInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter inbound toDomainUpdate Ok")
  void toDomainUpdateOkTest() {

    // INPUT
    UpdateAcademicItemDTO dtoInput = crearUpdateAcademicItemDTO("Calculadora");

    // EJECUCIÓN
    AcademicItem res = converter.toDomainUpdate(dtoInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertNull(res.getItemId());
          assertNull(res.getStudentId());
          assertEquals(dtoInput.getCategoryId(), res.getCategoryId());
          assertEquals(dtoInput.getTitle(), res.getTitle());
          assertEquals(dtoInput.getDescription(), res.getDescription());
          assertEquals(dtoInput.getPrice(), res.getPrice());
          assertEquals(dtoInput.getItemCondition(), res.getItemCondition());
          assertEquals(dtoInput.getListingType(), res.getListingType());
          assertEquals(dtoInput.getSaleStatus(), res.getSaleStatus());
          assertNotNull(res.getPublishedAt());
          assertEquals(dtoInput.getImageUrl(), res.getImageUrl());
        });
  }

  @Test
  @DisplayName("Test converter inbound toDomainUpdate Null")
  void toDomainUpdateNullTest() {

    // INPUT
    UpdateAcademicItemDTO dtoInput = null;

    // EJECUCIÓN
    AcademicItem res = converter.toDomainUpdate(dtoInput);

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
    Pageable pageableInput = PageRequest.of(0, 10, Sort.by("itemId").ascending());

    // EJECUCIÓN
    PageableModel res = converter.toPageableModel(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertEquals(0, res.getPage());
          assertEquals(10, res.getSize());
          assertEquals("itemId: ASC", res.getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toAcademicItemsPage Ok (sin ordenar)")
  void toAcademicItemsPageUnsortedOkTest() {

    // INPUT
    PageModel<AcademicItem> pageModelInput =
        new PageModel<>(
            List.of(
                crearAcademicItem(1, "Calculadora"), crearAcademicItem(2, "Libro de Matemáticas")),
            2,
            1,
            2,
            10,
            0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    Page<AcademicItemDTO> pageEsperado =
        new PageImpl<>(
            List.of(
                crearAcademicItemDTO(1, "Calculadora"),
                crearAcademicItemDTO(2, "Libro de Matemáticas")),
            PageRequest.of(0, 10),
            2);

    // EJECUCIÓN
    Page<AcademicItemDTO> res = converter.toAcademicItemsPage(pageModelInput, pageableModelInput);

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
  @DisplayName("Test converter inbound toAcademicItemsPage Ok (orden ascendente)")
  void toAcademicItemsPageAscOkTest() {

    // INPUT
    PageModel<AcademicItem> pageModelInput =
        new PageModel<>(List.of(crearAcademicItem(1, "Calculadora")), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "itemId: ASC");

    // ESPERADO
    Page<AcademicItemDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearAcademicItemDTO(1, "Calculadora")),
            PageRequest.of(0, 10, Sort.by("itemId").ascending()),
            1);

    // EJECUCIÓN
    Page<AcademicItemDTO> res = converter.toAcademicItemsPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("itemId").ascending(), res.getPageable().getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toAcademicItemsPage Ok (orden descendente)")
  void toAcademicItemsPageDescOkTest() {

    // INPUT
    PageModel<AcademicItem> pageModelInput =
        new PageModel<>(List.of(crearAcademicItem(1, "Calculadora")), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "itemId: DESC");

    // ESPERADO
    Page<AcademicItemDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearAcademicItemDTO(1, "Calculadora")),
            PageRequest.of(0, 10, Sort.by("itemId").descending()),
            1);

    // EJECUCIÓN
    Page<AcademicItemDTO> res = converter.toAcademicItemsPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("itemId").descending(), res.getPageable().getSort());
        });
  }
}
