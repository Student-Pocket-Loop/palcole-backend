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
import org.vedruna.palcole.domain.model.Category;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.CategoryEntity;

class CategoryOutConverterTest {

  private final CategoryOutConverter categoryOutConverter =
      Mappers.getMapper(CategoryOutConverter.class);

  private Category crearCategory(Integer categoryId, String name) {
    Category o = new Category();
    o.setCategoryId(categoryId);
    o.setName(name);
    return o;
  }

  private CategoryEntity crearCategoryEntity(Integer categoryId, String name) {
    CategoryEntity o = new CategoryEntity();
    o.setCategoryId(categoryId);
    o.setName(name);
    return o;
  }

  @Test
  @DisplayName("Test converter toDomain Ok")
  void toDomainOkTest() {

    // INPUT
    CategoryEntity categoryEntityInput = crearCategoryEntity(1, "Libros");

    // ESPERADO
    Category categoryEsperado = crearCategory(1, "Libros");

    // EJECUCIÓN
    Category res = categoryOutConverter.toDomain(categoryEntityInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(categoryEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toDomain Null")
  void toDomainNullTest() {

    // INPUT
    CategoryEntity categoryEntityInput = null;

    // EJECUCIÓN
    Category res = categoryOutConverter.toDomain(categoryEntityInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter toEntity Ok")
  void toEntityOkTest() {

    // INPUT
    Category categoryInput = crearCategory(1, "Libros");

    // ESPERADO
    CategoryEntity categoryEntityEsperado = crearCategoryEntity(1, "Libros");

    // EJECUCIÓN
    CategoryEntity res = categoryOutConverter.toEntity(categoryInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(categoryEntityEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toEntity Null")
  void toEntityNullTest() {

    // INPUT
    Category categoryInput = null;

    // EJECUCIÓN
    CategoryEntity res = categoryOutConverter.toEntity(categoryInput);

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
    Pageable res = categoryOutConverter.toPageable(pageableModelInput);

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
    PageableModel pageableModelInput = new PageableModel(0, 10, "categoryId: ASC");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by("categoryId").ascending());

    // EJECUCIÓN
    Pageable res = categoryOutConverter.toPageable(pageableModelInput);

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
    PageableModel pageableModelInput = new PageableModel(0, 10, "categoryId: DESC");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by("categoryId").descending());

    // EJECUCIÓN
    Pageable res = categoryOutConverter.toPageable(pageableModelInput);

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
        () -> categoryOutConverter.toPageable(pageableModelInput));
  }

  @Test
  @DisplayName("Test converter toCategoryPageModel Ok")
  void toCategoryPageModelOkTest() {

    // INPUT
    Pageable pageable = PageRequest.of(0, 10);
    Page<CategoryEntity> pageInput =
        new PageImpl<>(
            List.of(crearCategoryEntity(1, "Libros"), crearCategoryEntity(2, "Apuntes")),
            pageable,
            2);

    // ESPERADO
    PageModel<Category> pageModelEsperado =
        new PageModel<>(
            List.of(crearCategory(1, "Libros"), crearCategory(2, "Apuntes")), 2, 1, 2, 10, 0);

    // EJECUCIÓN
    PageModel<Category> res = categoryOutConverter.toCategoryPageModel(pageInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toCategoryPageModel Ko")
  void toCategoryPageModelKoTest() {

    // INPUT
    Page<CategoryEntity> pageInput = null;

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(
        NullPointerException.class, () -> categoryOutConverter.toCategoryPageModel(pageInput));
  }
}
