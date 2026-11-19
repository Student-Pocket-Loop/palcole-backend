package org.vedruna.palcole.infrastructure.adapters.inbound.rest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.vedruna.palcole.domain.exception.categories.CategoryNotFoundException;
import org.vedruna.palcole.domain.model.Category;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.categories.DeleteCategoryUseCase;
import org.vedruna.palcole.domain.ports.inbound.categories.NewCategoryUseCase;
import org.vedruna.palcole.domain.ports.inbound.categories.SelectAllCategoriesUseCase;
import org.vedruna.palcole.domain.ports.inbound.categories.SelectCategoryByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.categories.UpdateCategoryUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.CategoryInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.categories.CategoryDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.categories.InsertCategoryDTO;


@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

  @Mock private NewCategoryUseCase newCategoryUseCase;

  @Mock private DeleteCategoryUseCase deleteCategoryUseCase;

  @Mock private SelectAllCategoriesUseCase selectAllCategoriesUseCase;

  @Mock private SelectCategoryByIdUseCase selectCategoryByIdUseCase;

  @Mock private UpdateCategoryUseCase updateCategoryUseCase;

  @Mock private CategoryInConverter categoryInConverter;

  @InjectMocks private CategoryController categoryController;

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
  @DisplayName("Test controlador getAll Ok")
  void getAllOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10);

    // ESPERADO
    List<CategoryDTO> lista =
        List.of(crearCategoryDTO(1, "Libros"), crearCategoryDTO(2, "Apuntes"));

    Pageable pageableEsperado = PageRequest.of(0, 10);

    Page<CategoryDTO> pageEsperado = new PageImpl<>(lista, pageableEsperado, 2);

    ResponseEntity<Page<CategoryDTO>> resEsperado = ResponseEntity.ok(pageEsperado);

    // MOCKS
    // CONVERTER MOCK (Pageable -> PageableModel)
    PageableModel pageableModelMock = new PageableModel(0, 10, "UNSORTED");

    when(categoryInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);

    // USE CASE MOCK
    PageModel<Category> pageModelMock =
        new PageModel<>(
            List.of(crearCategory(1, "Libros"), crearCategory(2, "Apuntes")), 2, 1, 2, 10, 0);

    when(selectAllCategoriesUseCase.selectAllCategories(pageableModelMock))
        .thenReturn(pageModelMock);

    // CONVERTER MOCK (PageModel -> Page de DTOs)
    Page<CategoryDTO> pageDtoMock =
        new PageImpl<>(
            List.of(crearCategoryDTO(1, "Libros"), crearCategoryDTO(2, "Apuntes")),
            PageRequest.of(0, 10),
            2);

    when(categoryInConverter.toCategoriesPage(pageModelMock, pageableModelMock))
        .thenReturn(pageDtoMock);

    // EJECUCIÓN
    ResponseEntity<Page<CategoryDTO>> res = categoryController.getAllCategories(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.OK, res.getStatusCode());
          assertEquals(2, res.getBody().getContent().size());
          assertEquals(pageEsperado, res.getBody());
          assertEquals(resEsperado, res);
        });
  }

  @Test
  @DisplayName("Test controlador getAll Ko")
  void getAllKoTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10);

    // MOCKS
    PageableModel pageableModelMock = new PageableModel(0, 10, "UNSORTED");

    when(categoryInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);
    when(selectAllCategoriesUseCase.selectAllCategories(pageableModelMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> categoryController.getAllCategories(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(categoryInConverter, never()).toCategoriesPage(any(), any());
  }

  @Test
  @DisplayName("Test controlador insertIntoCategory Ok")
  void insertIntoCategoryOkTest() {

    // INPUT
    InsertCategoryDTO insertCategoryDTOInput = crearInsertCategoryDTO("Libros");

    // ESPERADO
    CategoryDTO dtoEsperado = crearCategoryDTO(3, "Libros");

    ResponseEntity<CategoryDTO> resEsperado =
        ResponseEntity.status(HttpStatus.CREATED).body(dtoEsperado);

    // MOCKS
    Category categoryNuevoMock = crearCategory(null, "Libros");
    Category categoryCreadoMock = crearCategory(3, "Libros");

    when(categoryInConverter.toDomainInsert(insertCategoryDTOInput)).thenReturn(categoryNuevoMock);
    when(newCategoryUseCase.newCategory(categoryNuevoMock)).thenReturn(categoryCreadoMock);
    when(categoryInConverter.toDto(categoryCreadoMock)).thenReturn(crearCategoryDTO(3, "Libros"));

    // EJECUCIÓN
    ResponseEntity<CategoryDTO> res = categoryController.insertIntoCategory(insertCategoryDTOInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.CREATED, res.getStatusCode());
          assertEquals(dtoEsperado, res.getBody());
          assertEquals(resEsperado, res);
        });
  }

  @Test
  @DisplayName("Test controlador insertIntoCategory Ko")
  void insertIntoCategoryKoTest() {

    // INPUT
    InsertCategoryDTO insertCategoryDTOInput = crearInsertCategoryDTO("Libros");

    // MOCKS
    Category categoryNuevoMock = crearCategory(null, "Libros");

    when(categoryInConverter.toDomainInsert(insertCategoryDTOInput)).thenReturn(categoryNuevoMock);
    when(newCategoryUseCase.newCategory(categoryNuevoMock))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> categoryController.insertIntoCategory(insertCategoryDTOInput));

    assertNotNull(ex.getMessage());
    verify(categoryInConverter, never()).toDto(any(Category.class));
  }

  @Test
  @DisplayName("Test controlador getCategoryById Ok")
  void getCategoryByIdOkTest() {

    // INPUT
    Integer categoryIdInput = 1;

    // ESPERADO
    CategoryDTO dtoEsperado = crearCategoryDTO(1, "Libros");

    ResponseEntity<CategoryDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    Category categoryMock = crearCategory(1, "Libros");

    when(selectCategoryByIdUseCase.selectCategoryById(categoryIdInput)).thenReturn(categoryMock);
    when(categoryInConverter.toDto(categoryMock)).thenReturn(crearCategoryDTO(1, "Libros"));

    // EJECUCIÓN
    ResponseEntity<CategoryDTO> res = categoryController.getCategoryById(categoryIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.OK, res.getStatusCode());
          assertEquals(dtoEsperado, res.getBody());
          assertEquals(resEsperado, res);
        });
  }

  @Test
  @DisplayName("Test controlador getCategoryById Ko")
  void getCategoryByIdKoTest() {

    // INPUT
    Integer categoryIdInput = 99;

    // MOCKS
    when(selectCategoryByIdUseCase.selectCategoryById(categoryIdInput))
        .thenThrow(new CategoryNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    CategoryNotFoundException ex =
        assertThrows(
            CategoryNotFoundException.class,
            () -> categoryController.getCategoryById(categoryIdInput));

    assertEquals("No se encuentra la categoría con id: 99", ex.getMessage());
    verify(categoryInConverter, never()).toDto(any(Category.class));
  }

  @Test
  @DisplayName("Test controlador updateCategory Ok")
  void updateCategoryOkTest() {

    // INPUT
    Integer categoryIdInput = 1;
    InsertCategoryDTO insertCategoryDTOInput = crearInsertCategoryDTO("Material escolar");

    // ESPERADO
    CategoryDTO dtoEsperado = crearCategoryDTO(1, "Material escolar");

    ResponseEntity<CategoryDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    Category categoryNuevosDatosMock = crearCategory(null, "Material escolar");
    Category categoryActualizadoMock = crearCategory(1, "Material escolar");

    when(categoryInConverter.toDomainInsert(insertCategoryDTOInput))
        .thenReturn(categoryNuevosDatosMock);
    when(updateCategoryUseCase.updateCategory(categoryIdInput, categoryNuevosDatosMock))
        .thenReturn(categoryActualizadoMock);
    when(categoryInConverter.toDto(categoryActualizadoMock))
        .thenReturn(crearCategoryDTO(1, "Material escolar"));

    // EJECUCIÓN
    ResponseEntity<CategoryDTO> res =
        categoryController.updateCategory(categoryIdInput, insertCategoryDTOInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.OK, res.getStatusCode());
          assertEquals(dtoEsperado, res.getBody());
          assertEquals(resEsperado, res);
        });
  }

  @Test
  @DisplayName("Test controlador updateCategory Ko")
  void updateCategoryKoTest() {

    // INPUT
    Integer categoryIdInput = 99;
    InsertCategoryDTO insertCategoryDTOInput = crearInsertCategoryDTO("Material escolar");

    // MOCKS
    Category categoryNuevosDatosMock = crearCategory(null, "Material escolar");

    when(categoryInConverter.toDomainInsert(insertCategoryDTOInput))
        .thenReturn(categoryNuevosDatosMock);
    when(updateCategoryUseCase.updateCategory(categoryIdInput, categoryNuevosDatosMock))
        .thenThrow(new CategoryNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    CategoryNotFoundException ex =
        assertThrows(
            CategoryNotFoundException.class,
            () -> categoryController.updateCategory(categoryIdInput, insertCategoryDTOInput));

    assertEquals("No se encuentra la categoría con id: 99", ex.getMessage());
    verify(categoryInConverter, never()).toDto(any(Category.class));
  }

  @Test
  @DisplayName("Test controlador deleteCategory Ok")
  void deleteCategoryOkTest() {

    // INPUT
    Integer categoryIdInput = 1;

    // ESPERADO
    ResponseEntity<Void> resEsperado = ResponseEntity.noContent().build();

    // MOCKS
    doNothing().when(deleteCategoryUseCase).deleteCategory(categoryIdInput);

    // EJECUCIÓN
    ResponseEntity<Void> res = categoryController.deleteCategory(categoryIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
          assertNull(res.getBody());
          assertEquals(resEsperado, res);
        });
    verify(deleteCategoryUseCase).deleteCategory(categoryIdInput);
  }

  @Test
  @DisplayName("Test controlador deleteCategory Ko")
  void deleteCategoryKoTest() {

    // INPUT
    Integer categoryIdInput = 99;

    // MOCKS
    doThrow(new CategoryNotFoundException(99))
        .when(deleteCategoryUseCase)
        .deleteCategory(categoryIdInput);

    // EJECUCIÓN Y VALIDACIONES
    CategoryNotFoundException ex =
        assertThrows(
            CategoryNotFoundException.class,
            () -> categoryController.deleteCategory(categoryIdInput));

    assertEquals("No se encuentra la categoría con id: 99", ex.getMessage());
  }
}
