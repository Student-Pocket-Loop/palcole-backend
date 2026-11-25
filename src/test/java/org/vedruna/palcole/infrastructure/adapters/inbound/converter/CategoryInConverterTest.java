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
import org.vedruna.palcole.domain.model.Category;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.categories.CategoryDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.categories.InsertCategoryDTO;

class CategoryInConverterTest {

  private final CategoryInConverter converter = Mappers.getMapper(CategoryInConverter.class);

  private Category crearCategory(Integer categoryId, String name) {
    Category o = new Category();
    o.setCategoryId(categoryId);
    o.setName(name);
    return o;
  }

  private CategoryDTO crearCategoryDTO(Integer categoryId, String name) {
    CategoryDTO o = new CategoryDTO();
    o.setCategoryId(categoryId);
    o.setName(name);
    return o;
  }

  private InsertCategoryDTO crearInsertCategoryDTO(String name) {
    InsertCategoryDTO o = new InsertCategoryDTO();
    o.setName(name);
    return o;
  }

  @Test
  @DisplayName("Test converter inbound toDto Ok")
  void toDtoOkTest() {

    // INPUT
    Category categoryInput = crearCategory(1, "Libros");

    // ESPERADO
    CategoryDTO dtoEsperado = crearCategoryDTO(1, "Libros");

    // EJECUCIÓN
    CategoryDTO res = converter.toDto(categoryInput);

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
    Category categoryInput = null;

    // EJECUCIÓN
    CategoryDTO res = converter.toDto(categoryInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter inbound toDomainInsert Ok")
  void toDomainInsertOkTest() {

    // INPUT
    InsertCategoryDTO dtoInput = crearInsertCategoryDTO("Libros");

    // EJECUCIÓN
    Category res = converter.toDomainInsert(dtoInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertNull(res.getCategoryId());
          assertEquals(dtoInput.getName(), res.getName());
        });
  }

  @Test
  @DisplayName("Test converter inbound toDomainInsert Null")
  void toDomainInsertNullTest() {

    // INPUT
    InsertCategoryDTO dtoInput = null;

    // EJECUCIÓN
    Category res = converter.toDomainInsert(dtoInput);

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
    Pageable pageableInput = PageRequest.of(0, 10, Sort.by("categoryId").ascending());

    // EJECUCIÓN
    PageableModel res = converter.toPageableModel(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertEquals(0, res.getPage());
          assertEquals(10, res.getSize());
          assertEquals("categoryId: ASC", res.getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toCategoriesPage Ok (sin ordenar)")
  void toCategoriesPageUnsortedOkTest() {

    // INPUT
    PageModel<Category> pageModelInput =
        new PageModel<>(
            List.of(crearCategory(1, "Libros"), crearCategory(2, "Apuntes")), 2, 1, 2, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    Page<CategoryDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearCategoryDTO(1, "Libros"), crearCategoryDTO(2, "Apuntes")),
            PageRequest.of(0, 10),
            2);

    // EJECUCIÓN
    Page<CategoryDTO> res = converter.toCategoriesPage(pageModelInput, pageableModelInput);

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
  @DisplayName("Test converter inbound toCategoriesPage Ok (orden ascendente)")
  void toCategoriesPageAscOkTest() {

    // INPUT
    PageModel<Category> pageModelInput =
        new PageModel<>(List.of(crearCategory(1, "Libros")), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "categoryId: ASC");

    // ESPERADO
    Page<CategoryDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearCategoryDTO(1, "Libros")),
            PageRequest.of(0, 10, Sort.by("categoryId").ascending()),
            1);

    // EJECUCIÓN
    Page<CategoryDTO> res = converter.toCategoriesPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("categoryId").ascending(), res.getPageable().getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toCategoriesPage Ok (orden descendente)")
  void toCategoriesPageDescOkTest() {

    // INPUT
    PageModel<Category> pageModelInput =
        new PageModel<>(List.of(crearCategory(1, "Libros")), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "categoryId: DESC");

    // ESPERADO
    Page<CategoryDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearCategoryDTO(1, "Libros")),
            PageRequest.of(0, 10, Sort.by("categoryId").descending()),
            1);

    // EJECUCIÓN
    Page<CategoryDTO> res = converter.toCategoriesPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("categoryId").descending(), res.getPageable().getSort());
        });
  }
}
