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

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import org.vedruna.palcole.domain.exception.academicItems.AcademicItemNotFoundException;
import org.vedruna.palcole.domain.model.AcademicItem;
import org.vedruna.palcole.domain.model.enums.ItemCondition;
import org.vedruna.palcole.domain.model.enums.ListingType;
import org.vedruna.palcole.domain.model.enums.SaleStatus;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.academicItems.DeleteAcademicItemUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.NewAcademicItemUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.SelectAcademicItemByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.SelectAllAcademicItemsUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.UpdateAcademicItemUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.AcademicItemInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems.AcademicItemDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems.InsertAcademicItemDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems.UpdateAcademicItemDTO;

@ExtendWith(MockitoExtension.class)
class AcademicItemControllerTest {

  @Mock private NewAcademicItemUseCase newAcademicItemUseCase;

  @Mock private DeleteAcademicItemUseCase deleteAcademicItemUseCase;

  @Mock private SelectAllAcademicItemsUseCase selectAllAcademicItemsUseCase;

  @Mock private SelectAcademicItemByIdUseCase selectAcademicItemByIdUseCase;

  @Mock private UpdateAcademicItemUseCase updateAcademicItemUseCase;

  @Mock private AcademicItemInConverter academicItemInConverter;

  @InjectMocks private AcademicItemController academicItemController;

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
  @DisplayName("Test controlador getAll Ok")
  void getAllOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10);

    // ESPERADO
    List<AcademicItemDTO> lista =
        List.of(
            crearAcademicItemDTO(1, "Calculadora"),
            crearAcademicItemDTO(2, "Libro de Matemáticas"));

    Pageable pageableEsperado = PageRequest.of(0, 10);

    Page<AcademicItemDTO> pageEsperado = new PageImpl<>(lista, pageableEsperado, 2);

    ResponseEntity<Page<AcademicItemDTO>> resEsperado = ResponseEntity.ok(pageEsperado);

    // MOCKS
    // CONVERTER MOCK (Pageable -> PageableModel)
    PageableModel pageableModelMock = new PageableModel(0, 10, "UNSORTED");

    when(academicItemInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);

    // USE CASE MOCK
    PageModel<AcademicItem> pageModelMock =
        new PageModel<>(
            List.of(
                crearAcademicItem(1, "Calculadora"), crearAcademicItem(2, "Libro de Matemáticas")),
            2,
            1,
            2,
            10,
            0);

    when(selectAllAcademicItemsUseCase.selectAllAcademicItems(pageableModelMock))
        .thenReturn(pageModelMock);

    // CONVERTER MOCK (PageModel -> Page de DTOs)
    Page<AcademicItemDTO> pageDtoMock =
        new PageImpl<>(
            List.of(
                crearAcademicItemDTO(1, "Calculadora"),
                crearAcademicItemDTO(2, "Libro de Matemáticas")),
            PageRequest.of(0, 10),
            2);

    when(academicItemInConverter.toAcademicItemsPage(pageModelMock, pageableModelMock))
        .thenReturn(pageDtoMock);

    // EJECUCIÓN
    ResponseEntity<Page<AcademicItemDTO>> res =
        academicItemController.getAllAcademicItems(pageableInput);

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

    when(academicItemInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);
    when(selectAllAcademicItemsUseCase.selectAllAcademicItems(pageableModelMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> academicItemController.getAllAcademicItems(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(academicItemInConverter, never()).toAcademicItemsPage(any(), any());
  }

  @Test
  @DisplayName("Test controlador insertIntoAcademicItem Ok")
  void insertIntoAcademicItemOkTest() {

    // INPUT
    InsertAcademicItemDTO insertAcademicItemDTOInput = crearInsertAcademicItemDTO("Calculadora");

    // ESPERADO
    AcademicItemDTO dtoEsperado = crearAcademicItemDTO(3, "Calculadora");

    ResponseEntity<AcademicItemDTO> resEsperado =
        ResponseEntity.status(HttpStatus.CREATED).body(dtoEsperado);

    // MOCKS
    AcademicItem academicItemNuevoMock = crearAcademicItem(null, "Calculadora");
    AcademicItem academicItemCreadoMock = crearAcademicItem(3, "Calculadora");

    when(academicItemInConverter.toDomainInsert(insertAcademicItemDTOInput))
        .thenReturn(academicItemNuevoMock);
    when(newAcademicItemUseCase.newAcademicItem(academicItemNuevoMock))
        .thenReturn(academicItemCreadoMock);
    when(academicItemInConverter.toDto(academicItemCreadoMock))
        .thenReturn(crearAcademicItemDTO(3, "Calculadora"));

    // EJECUCIÓN
    ResponseEntity<AcademicItemDTO> res =
        academicItemController.insertIntoAcademicItem(insertAcademicItemDTOInput);

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
  @DisplayName("Test controlador insertIntoAcademicItem Ko")
  void insertIntoAcademicItemKoTest() {

    // INPUT
    InsertAcademicItemDTO insertAcademicItemDTOInput = crearInsertAcademicItemDTO("Calculadora");

    // MOCKS
    AcademicItem academicItemNuevoMock = crearAcademicItem(null, "Calculadora");

    when(academicItemInConverter.toDomainInsert(insertAcademicItemDTOInput))
        .thenReturn(academicItemNuevoMock);
    when(newAcademicItemUseCase.newAcademicItem(academicItemNuevoMock))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> academicItemController.insertIntoAcademicItem(insertAcademicItemDTOInput));

    assertNotNull(ex.getMessage());
    verify(academicItemInConverter, never()).toDto(any(AcademicItem.class));
  }

  @Test
  @DisplayName("Test controlador getAcademicItemById Ok")
  void getAcademicItemByIdOkTest() {

    // INPUT
    Integer itemIdInput = 1;

    // ESPERADO
    AcademicItemDTO dtoEsperado = crearAcademicItemDTO(1, "Calculadora");

    ResponseEntity<AcademicItemDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    AcademicItem academicItemMock = crearAcademicItem(1, "Calculadora");

    when(selectAcademicItemByIdUseCase.selectAcademicItemById(itemIdInput))
        .thenReturn(academicItemMock);
    when(academicItemInConverter.toDto(academicItemMock))
        .thenReturn(crearAcademicItemDTO(1, "Calculadora"));

    // EJECUCIÓN
    ResponseEntity<AcademicItemDTO> res = academicItemController.getAcademicItemById(itemIdInput);

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
  @DisplayName("Test controlador getAcademicItemById Ko")
  void getAcademicItemByIdKoTest() {

    // INPUT
    Integer itemIdInput = 99;

    // MOCKS
    when(selectAcademicItemByIdUseCase.selectAcademicItemById(itemIdInput))
        .thenThrow(new AcademicItemNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    AcademicItemNotFoundException ex =
        assertThrows(
            AcademicItemNotFoundException.class,
            () -> academicItemController.getAcademicItemById(itemIdInput));

    assertEquals("No se encuentra el artículo con id: 99", ex.getMessage());
    verify(academicItemInConverter, never()).toDto(any(AcademicItem.class));
  }

  @Test
  @DisplayName("Test controlador updateAcademicItem Ok")
  void updateAcademicItemOkTest() {

    // INPUT
    Integer itemIdInput = 1;
    UpdateAcademicItemDTO updateAcademicItemDTOInput =
        crearUpdateAcademicItemDTO("Calculadora actualizada");

    // ESPERADO
    AcademicItemDTO dtoEsperado = crearAcademicItemDTO(1, "Calculadora actualizada");

    ResponseEntity<AcademicItemDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    AcademicItem academicItemNuevosDatosMock = crearAcademicItem(null, "Calculadora actualizada");
    AcademicItem academicItemActualizadoMock = crearAcademicItem(1, "Calculadora actualizada");

    when(academicItemInConverter.toDomainUpdate(updateAcademicItemDTOInput))
        .thenReturn(academicItemNuevosDatosMock);
    when(updateAcademicItemUseCase.updateAcademicItem(itemIdInput, academicItemNuevosDatosMock))
        .thenReturn(academicItemActualizadoMock);
    when(academicItemInConverter.toDto(academicItemActualizadoMock))
        .thenReturn(crearAcademicItemDTO(1, "Calculadora actualizada"));

    // EJECUCIÓN
    ResponseEntity<AcademicItemDTO> res =
        academicItemController.updateAcademicItem(itemIdInput, updateAcademicItemDTOInput);

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
  @DisplayName("Test controlador updateAcademicItem Ko")
  void updateAcademicItemKoTest() {

    // INPUT
    Integer itemIdInput = 99;
    UpdateAcademicItemDTO updateAcademicItemDTOInput =
        crearUpdateAcademicItemDTO("Calculadora actualizada");

    // MOCKS
    AcademicItem academicItemNuevosDatosMock = crearAcademicItem(null, "Calculadora actualizada");

    when(academicItemInConverter.toDomainUpdate(updateAcademicItemDTOInput))
        .thenReturn(academicItemNuevosDatosMock);
    when(updateAcademicItemUseCase.updateAcademicItem(itemIdInput, academicItemNuevosDatosMock))
        .thenThrow(new AcademicItemNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    AcademicItemNotFoundException ex =
        assertThrows(
            AcademicItemNotFoundException.class,
            () ->
                academicItemController.updateAcademicItem(itemIdInput, updateAcademicItemDTOInput));

    assertEquals("No se encuentra el artículo con id: 99", ex.getMessage());
    verify(academicItemInConverter, never()).toDto(any(AcademicItem.class));
  }

  @Test
  @DisplayName("Test controlador deleteAcademicItem Ok")
  void deleteAcademicItemOkTest() {

    // INPUT
    Integer itemIdInput = 1;

    // ESPERADO
    ResponseEntity<Void> resEsperado = ResponseEntity.noContent().build();

    // MOCKS
    doNothing().when(deleteAcademicItemUseCase).deleteAcademicItem(itemIdInput);

    // EJECUCIÓN
    ResponseEntity<Void> res = academicItemController.deleteAcademicItem(itemIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
          assertNull(res.getBody());
          assertEquals(resEsperado, res);
        });
    verify(deleteAcademicItemUseCase).deleteAcademicItem(itemIdInput);
  }

  @Test
  @DisplayName("Test controlador deleteAcademicItem Ko")
  void deleteAcademicItemKoTest() {

    // INPUT
    Integer itemIdInput = 99;

    // MOCKS
    doThrow(new AcademicItemNotFoundException(99))
        .when(deleteAcademicItemUseCase)
        .deleteAcademicItem(itemIdInput);

    // EJECUCIÓN Y VALIDACIONES
    AcademicItemNotFoundException ex =
        assertThrows(
            AcademicItemNotFoundException.class,
            () -> academicItemController.deleteAcademicItem(itemIdInput));

    assertEquals("No se encuentra el artículo con id: 99", ex.getMessage());
  }
}
