package org.vedruna.palcole.infrastructure.adapters.inbound.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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

@Tag(
    name = "Categories",
    description = "Gestión de categorías académicas (libros, electrónica, material escolar, etc.)")
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

  @Operation(
      summary = "Listar todas las categorías (paginado)",
      description =
          "Devuelve una página con las categorías registradas. Permite paginación y ordenación mediante parámetros query.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista paginada de categorías recuperada correctamente"),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de paginación u ordenación inválidos",
            content = @Content)
      })
  @GetMapping("/")
  public ResponseEntity<Page<CategoryDTO>> getAllCategories(@ParameterObject Pageable pageable) {
    PageableModel pageableModel = categoryInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        categoryInConverter.toCategoriesPage(
            selectAllCategoriesUseCase.selectAllCategories(pageableModel), pageableModel));
  }

  @Operation(
      summary = "Crear una nueva categoría",
      description = "Registra una nueva categoría académica en el sistema. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Categoría creada exitosamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CategoryDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Categoría creada (ADMIN)",
                          summary = "Respuesta tras crear la categoría Idiomas",
                          value =
                              """
                              {
                                "categoryId": 4,
                                "name": "Idiomas"
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos o campos obligatorios ausentes",
            content = @Content),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado (token JWT ausente o inválido)",
            content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol ADMIN, denegado para ROLE_USER)",
            content = @Content)
      })
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/")
  public ResponseEntity<CategoryDTO> insertIntoCategory(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Nombre de la nueva categoría",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = InsertCategoryDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Nueva Categoría (Idiomas)",
                            summary = "Ejemplo de creación de categoría",
                            value =
                                """
                                {
                                    "name": "Idiomas"
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          InsertCategoryDTO insertCategoryDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            categoryInConverter.toDto(
                newCategoryUseCase.newCategory(
                    categoryInConverter.toDomainInsert(insertCategoryDTO))));
  }

  @Operation(
      summary = "Obtener una categoría por su ID",
      description =
          "Busca y devuelve los detalles de una categoría específica a partir de su identificador.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoría encontrada",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CategoryDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Detalle de Categoría",
                          summary = "Ejemplo de respuesta al consultar categoría 1",
                          value =
                              """
                              {
                                "categoryId": 1,
                                "name": "Libros de texto"
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ninguna categoría con el ID proporcionado",
            content = @Content)
      })
  @GetMapping("/{categoryId}")
  public ResponseEntity<CategoryDTO> getCategoryById(
      @Parameter(description = "ID único de la categoría", example = "1") @PathVariable
          Integer categoryId) {
    return ResponseEntity.ok(
        categoryInConverter.toDto(selectCategoryByIdUseCase.selectCategoryById(categoryId)));
  }

  @Operation(
      summary = "Actualizar una categoría",
      description = "Modifica los datos de una categoría existente. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoría actualizada correctamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CategoryDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Categoría actualizada (ADMIN)",
                          summary = "Respuesta tras actualizar nombre a Idiomas y diccionarios",
                          value =
                              """
                              {
                                "categoryId": 1,
                                "name": "Idiomas y diccionarios"
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de actualización inválidos",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol ADMIN, denegado para ROLE_USER)",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ninguna categoría con el ID proporcionado",
            content = @Content)
      })
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{categoryId}")
  public ResponseEntity<CategoryDTO> updateCategory(
      @Parameter(description = "ID único de la categoría a actualizar", example = "1") @PathVariable
          Integer categoryId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Nuevo nombre de la categoría",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = InsertCategoryDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Actualizar Categoría",
                            summary = "Ejemplo de actualización de nombre",
                            value =
                                """
                                {
                                    "name": "Idiomas y diccionarios"
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          InsertCategoryDTO insertCategoryDTO) {
    return ResponseEntity.ok(
        categoryInConverter.toDto(
            updateCategoryUseCase.updateCategory(
                categoryId, categoryInConverter.toDomainInsert(insertCategoryDTO))));
  }

  @Operation(
      summary = "Eliminar una categoría",
      description = "Elimina permanentemente una categoría por su ID. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "Categoría eliminada correctamente (sin contenido)",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol ADMIN)",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ninguna categoría con el ID proporcionado",
            content = @Content)
      })
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Void> deleteCategory(
      @Parameter(description = "ID único de la categoría a eliminar", example = "1") @PathVariable
          Integer categoryId) {
    deleteCategoryUseCase.deleteCategory(categoryId);
    return ResponseEntity.noContent().build();
  }
}
