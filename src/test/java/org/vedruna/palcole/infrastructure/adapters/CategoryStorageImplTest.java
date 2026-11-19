package org.vedruna.palcole.infrastructure.adapters;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
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
import org.vedruna.palcole.domain.exception.categories.CategoryNotFoundException;
import org.vedruna.palcole.domain.model.Category;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.CategoryStorageImpl;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.CategoryOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.CategoryEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryStorageImplTest {

  @Mock private CategoryRepository categoryRepository;

  @Mock private CategoryOutConverter categoryOutConverter;

  @InjectMocks private CategoryStorageImpl categoryStorageImpl;

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
  @DisplayName("Test storage selectAllCategories Ok")
  void selectAllCategoriesOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<Category> pageModelEsperado =
        new PageModel<>(
            List.of(crearCategory(1, "Libros"), crearCategory(2, "Apuntes")), 2, 1, 2, 10, 0);

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);
    Page<CategoryEntity> pageEntityMock =
        new PageImpl<>(
            List.of(crearCategoryEntity(1, "Libros"), crearCategoryEntity(2, "Apuntes")),
            pageableMock,
            2);
    PageModel<Category> pageModelMock =
        new PageModel<>(
            List.of(crearCategory(1, "Libros"), crearCategory(2, "Apuntes")), 2, 1, 2, 10, 0);

    when(categoryOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(categoryRepository.findAll(pageableMock)).thenReturn(pageEntityMock);
    when(categoryOutConverter.toCategoryPageModel(pageEntityMock)).thenReturn(pageModelMock);

    // EJECUCIÓN
    PageModel<Category> res = categoryStorageImpl.selectAllCategories(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage selectAllCategories Ko")
  void selectAllCategoriesKoTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "INVALID_SORT");

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);

    when(categoryOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(categoryRepository.findAll(pageableMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> categoryStorageImpl.selectAllCategories(pageableModelInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(categoryOutConverter, never()).toCategoryPageModel(any());
  }

  @Test
  @DisplayName("Test storage findCategoryById Ok")
  void findCategoryByIdOkTest() {

    // INPUT
    Integer categoryIdInput = 1;

    // ESPERADO
    Category categoryEsperado = crearCategory(1, "Libros");

    // MOCKS
    CategoryEntity categoryEntityMock = crearCategoryEntity(1, "Libros");
    Category categoryMock = crearCategory(1, "Libros");

    when(categoryRepository.findById(categoryIdInput)).thenReturn(Optional.of(categoryEntityMock));
    when(categoryOutConverter.toDomain(categoryEntityMock)).thenReturn(categoryMock);

    // EJECUCIÓN
    Category res = categoryStorageImpl.findCategoryById(categoryIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(categoryEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage findCategoryById Ko")
  void findCategoryByIdKoTest() {

    // INPUT
    Integer categoryIdInput = 99;

    // MOCKS
    when(categoryRepository.findById(categoryIdInput)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    CategoryNotFoundException ex =
        assertThrows(
            CategoryNotFoundException.class,
            () -> categoryStorageImpl.findCategoryById(categoryIdInput));

    assertEquals("No se encuentra la categoría con id: 99", ex.getMessage());
    verify(categoryOutConverter, never()).toDomain(any(CategoryEntity.class));
  }

  @Test
  @DisplayName("Test storage updateCategory Ok")
  void updateCategoryOkTest() {

    // INPUT
    Integer categoryIdInput = 1;
    Category newCategoryInput = crearCategory(null, "Material escolar");

    // ESPERADO
    Category categoryEsperado = crearCategory(1, "Material escolar");

    // MOCKS
    CategoryEntity categoryEntityExistenteMock = crearCategoryEntity(1, "Libros");
    CategoryEntity categoryEntityGuardadoMock = crearCategoryEntity(1, "Material escolar");
    Category categoryActualizadoMock = crearCategory(1, "Material escolar");

    when(categoryRepository.findById(categoryIdInput))
        .thenReturn(Optional.of(categoryEntityExistenteMock));
    when(categoryRepository.save(categoryEntityExistenteMock))
        .thenReturn(categoryEntityGuardadoMock);
    when(categoryOutConverter.toDomain(categoryEntityGuardadoMock))
        .thenReturn(categoryActualizadoMock);

    // EJECUCIÓN
    Category res = categoryStorageImpl.updateCategory(categoryIdInput, newCategoryInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals("Material escolar", categoryEntityExistenteMock.getName());
          assertEquals(categoryEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage updateCategory Ko")
  void updateCategoryKoTest() {

    // INPUT
    Integer categoryIdInput = 99;
    Category newCategoryInput = crearCategory(null, "Material escolar");

    // MOCKS
    when(categoryRepository.findById(categoryIdInput)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    CategoryNotFoundException ex =
        assertThrows(
            CategoryNotFoundException.class,
            () -> categoryStorageImpl.updateCategory(categoryIdInput, newCategoryInput));

    assertEquals("No se encuentra la categoría con id: 99", ex.getMessage());
    verify(categoryRepository, never()).save(any(CategoryEntity.class));
    verify(categoryOutConverter, never()).toDomain(any(CategoryEntity.class));
  }

  @Test
  @DisplayName("Test storage insertCategory Ok")
  void insertCategoryOkTest() {

    // INPUT
    Category newCategoryInput = crearCategory(null, "Libros");

    // ESPERADO
    Category categoryEsperado = crearCategory(3, "Libros");

    // MOCKS
    CategoryEntity categoryEntityNuevoMock = crearCategoryEntity(null, "Libros");
    CategoryEntity categoryEntityGuardadoMock = crearCategoryEntity(3, "Libros");
    Category categoryCreadoMock = crearCategory(3, "Libros");

    when(categoryOutConverter.toEntity(newCategoryInput)).thenReturn(categoryEntityNuevoMock);
    when(categoryRepository.save(categoryEntityNuevoMock)).thenReturn(categoryEntityGuardadoMock);
    when(categoryOutConverter.toDomain(categoryEntityGuardadoMock)).thenReturn(categoryCreadoMock);

    // EJECUCIÓN
    Category res = categoryStorageImpl.insertCategory(newCategoryInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(categoryEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage insertCategory Ko")
  void insertCategoryKoTest() {

    // INPUT
    Category newCategoryInput = crearCategory(null, "Libros");

    // MOCKS
    CategoryEntity categoryEntityNuevoMock = crearCategoryEntity(null, "Libros");

    when(categoryOutConverter.toEntity(newCategoryInput)).thenReturn(categoryEntityNuevoMock);
    when(categoryRepository.save(categoryEntityNuevoMock))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> categoryStorageImpl.insertCategory(newCategoryInput));

    assertEquals("Conflicto con los datos existentes", ex.getMessage());
    verify(categoryOutConverter, never()).toDomain(any(CategoryEntity.class));
  }

  @Test
  @DisplayName("Test storage deleteCategory Ok")
  void deleteCategoryOkTest() {

    // INPUT
    Integer categoryIdInput = 1;

    // MOCKS
    when(categoryRepository.existsById(categoryIdInput)).thenReturn(true);
    doNothing().when(categoryRepository).deleteById(categoryIdInput);

    // EJECUCIÓN
    categoryStorageImpl.deleteCategory(categoryIdInput);

    // VALIDACIONES
    verify(categoryRepository).existsById(categoryIdInput);
    verify(categoryRepository).deleteById(categoryIdInput);
  }

  @Test
  @DisplayName("Test storage deleteCategory Ko")
  void deleteCategoryKoTest() {

    // INPUT
    Integer categoryIdInput = 99;

    // MOCKS
    when(categoryRepository.existsById(categoryIdInput)).thenReturn(false);

    // EJECUCIÓN Y VALIDACIONES
    CategoryNotFoundException ex =
        assertThrows(
            CategoryNotFoundException.class,
            () -> categoryStorageImpl.deleteCategory(categoryIdInput));

    assertEquals("No se encuentra la categoría con id: 99", ex.getMessage());
    verify(categoryRepository, never()).deleteById(any());
  }
}
