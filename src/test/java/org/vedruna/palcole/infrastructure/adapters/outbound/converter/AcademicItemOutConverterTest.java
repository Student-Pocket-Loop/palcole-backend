package org.vedruna.palcole.infrastructure.adapters.outbound.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.AcademicItemEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.CategoryEntity;
import org.vedruna.palcole.persistance.model.UserEntity;

class AcademicItemOutConverterTest {

  private final AcademicItemOutConverter academicItemOutConverter =
      Mappers.getMapper(AcademicItemOutConverter.class);

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

  private AcademicItemEntity crearAcademicItemEntity(Integer itemId, String title) {
    AcademicItemEntity o = new AcademicItemEntity();
    o.setItemId(itemId);
    UserEntity studentRef = new UserEntity();
    studentRef.setUserId(1);
    o.setStudent(studentRef);
    CategoryEntity categoryRef = new CategoryEntity();
    categoryRef.setCategoryId(1);
    o.setCategory(categoryRef);
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

  @Test
  @DisplayName("Test converter toDomain Ok")
  void toDomainOkTest() {

    // INPUT
    AcademicItemEntity academicItemEntityInput = crearAcademicItemEntity(1, "Calculadora");

    // ESPERADO
    AcademicItem academicItemEsperado = crearAcademicItem(1, "Calculadora");

    // EJECUCIÓN
    AcademicItem res = academicItemOutConverter.toDomain(academicItemEntityInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(academicItemEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toDomain Null")
  void toDomainNullTest() {

    // INPUT
    AcademicItemEntity academicItemEntityInput = null;

    // EJECUCIÓN
    AcademicItem res = academicItemOutConverter.toDomain(academicItemEntityInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter toEntity Ok")
  void toEntityOkTest() {

    // INPUT
    AcademicItem academicItemInput = crearAcademicItem(1, "Calculadora");

    // ESPERADO
    AcademicItemEntity academicItemEntityEsperado = crearAcademicItemEntity(1, "Calculadora");
    academicItemEntityEsperado.setStudent(null); // la relación la resuelve el storage
    academicItemEntityEsperado.setCategory(null); // la relación la resuelve el storage

    // EJECUCIÓN
    AcademicItemEntity res = academicItemOutConverter.toEntity(academicItemInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(academicItemEntityEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toEntity Null")
  void toEntityNullTest() {

    // INPUT
    AcademicItem academicItemInput = null;

    // EJECUCIÓN
    AcademicItemEntity res = academicItemOutConverter.toEntity(academicItemInput);

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
    Pageable res = academicItemOutConverter.toPageable(pageableModelInput);

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
    PageableModel pageableModelInput = new PageableModel(0, 10, "itemId: ASC");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by("itemId").ascending());

    // EJECUCIÓN
    Pageable res = academicItemOutConverter.toPageable(pageableModelInput);

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
    PageableModel pageableModelInput = new PageableModel(0, 10, "itemId: DESC");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by("itemId").descending());

    // EJECUCIÓN
    Pageable res = academicItemOutConverter.toPageable(pageableModelInput);

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
        () -> academicItemOutConverter.toPageable(pageableModelInput));
  }

  @Test
  @DisplayName("Test converter toAcademicItemPageModel Ok")
  void toAcademicItemPageModelOkTest() {

    // INPUT
    Pageable pageable = PageRequest.of(0, 10);
    Page<AcademicItemEntity> pageInput =
        new PageImpl<>(
            List.of(
                crearAcademicItemEntity(1, "Calculadora"),
                crearAcademicItemEntity(2, "Libro de Matemáticas")),
            pageable,
            2);

    // ESPERADO
    PageModel<AcademicItem> pageModelEsperado =
        new PageModel<>(
            List.of(
                crearAcademicItem(1, "Calculadora"), crearAcademicItem(2, "Libro de Matemáticas")),
            2,
            1,
            2,
            10,
            0);

    // EJECUCIÓN
    PageModel<AcademicItem> res = academicItemOutConverter.toAcademicItemPageModel(pageInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toAcademicItemPageModel Ko")
  void toAcademicItemPageModelKoTest() {

    // INPUT
    Page<AcademicItemEntity> pageInput = null;

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(
        NullPointerException.class,
        () -> academicItemOutConverter.toAcademicItemPageModel(pageInput));
  }
}
