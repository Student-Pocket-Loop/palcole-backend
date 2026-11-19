package org.vedruna.palcole.infrastructure.adapters.inbound.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.categories.DeleteCategoryUseCase;
import org.vedruna.palcole.domain.ports.inbound.categories.NewCategoryUseCase;
import org.vedruna.palcole.domain.ports.inbound.categories.SelectAllCategoriesUseCase;
import org.vedruna.palcole.domain.ports.inbound.categories.SelectCategoryByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.categories.UpdateCategoryUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.CategoryInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.categories.CategoryDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.categories.InsertCategoryDTO;

@CrossOrigin
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

  private final NewCategoryUseCase newCategoryUseCase;
  private final DeleteCategoryUseCase deleteCategoryUseCase;
  private final SelectAllCategoriesUseCase selectAllCategoriesUseCase;
  private final SelectCategoryByIdUseCase selectCategoryByIdUseCase;
  private final UpdateCategoryUseCase updateCategoryUseCase;
  private final CategoryInConverter categoryInConverter;

  @GetMapping("/")
  public ResponseEntity<Page<CategoryDTO>> getAllCategories(Pageable pageable) {
    PageableModel pageableModel = categoryInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        categoryInConverter.toCategoriesPage(
            selectAllCategoriesUseCase.selectAllCategories(pageableModel), pageableModel));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/")
  public ResponseEntity<CategoryDTO> insertIntoCategory(
      @RequestBody @Valid InsertCategoryDTO insertCategoryDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            categoryInConverter.toDto(
                newCategoryUseCase.newCategory(
                    categoryInConverter.toDomainInsert(insertCategoryDTO))));
  }

  @GetMapping("/{categoryId}")
  public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Integer categoryId) {
    return ResponseEntity.ok(
        categoryInConverter.toDto(selectCategoryByIdUseCase.selectCategoryById(categoryId)));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{categoryId}")
  public ResponseEntity<CategoryDTO> updateCategory(
      @PathVariable Integer categoryId, @RequestBody @Valid InsertCategoryDTO insertCategoryDTO) {
    return ResponseEntity.ok(
        categoryInConverter.toDto(
            updateCategoryUseCase.updateCategory(
                categoryId, categoryInConverter.toDomainInsert(insertCategoryDTO))));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Void> deleteCategory(@PathVariable Integer categoryId) {
    deleteCategoryUseCase.deleteCategory(categoryId);
    return ResponseEntity.noContent().build();
  }
}
