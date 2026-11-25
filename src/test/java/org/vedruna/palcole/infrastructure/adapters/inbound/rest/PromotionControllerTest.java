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
import java.time.LocalDate;
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
import org.vedruna.palcole.domain.exception.promotions.PromotionNotFoundException;
import org.vedruna.palcole.domain.model.Promotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.promotions.DeletePromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.NewPromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.SelectAllPromotionsUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.SelectPromotionByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.UpdatePromotionUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.PromotionInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.promotions.InsertPromotionDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.promotions.PromotionDTO;

@ExtendWith(MockitoExtension.class)
class PromotionControllerTest {

  @Mock private NewPromotionUseCase newPromotionUseCase;

  @Mock private DeletePromotionUseCase deletePromotionUseCase;

  @Mock private SelectAllPromotionsUseCase selectAllPromotionsUseCase;

  @Mock private SelectPromotionByIdUseCase selectPromotionByIdUseCase;

  @Mock private UpdatePromotionUseCase updatePromotionUseCase;

  @Mock private PromotionInConverter promotionInConverter;

  @InjectMocks private PromotionController promotionController;

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
  @DisplayName("Test controlador getAll Ok")
  void getAllOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10);

    // ESPERADO
    List<PromotionDTO> lista =
        List.of(crearPromotionDTO(1, "Papelería"), crearPromotionDTO(2, "Mochilas"));

    Pageable pageableEsperado = PageRequest.of(0, 10);

    Page<PromotionDTO> pageEsperado = new PageImpl<>(lista, pageableEsperado, 2);

    ResponseEntity<Page<PromotionDTO>> resEsperado = ResponseEntity.ok(pageEsperado);

    // MOCKS
    // CONVERTER MOCK (Pageable -> PageableModel)
    PageableModel pageableModelMock = new PageableModel(0, 10, "UNSORTED");

    when(promotionInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);

    // USE CASE MOCK
    PageModel<Promotion> pageModelMock =
        new PageModel<>(
            List.of(crearPromotion(1, "Papelería"), crearPromotion(2, "Mochilas")), 2, 1, 2, 10, 0);

    when(selectAllPromotionsUseCase.selectAllPromotions(pageableModelMock))
        .thenReturn(pageModelMock);

    // CONVERTER MOCK (PageModel -> Page de DTOs)
    Page<PromotionDTO> pageDtoMock =
        new PageImpl<>(
            List.of(crearPromotionDTO(1, "Papelería"), crearPromotionDTO(2, "Mochilas")),
            PageRequest.of(0, 10),
            2);

    when(promotionInConverter.toPromotionsPage(pageModelMock, pageableModelMock))
        .thenReturn(pageDtoMock);

    // EJECUCIÓN
    ResponseEntity<Page<PromotionDTO>> res = promotionController.getAllPromotions(pageableInput);

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

    when(promotionInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);
    when(selectAllPromotionsUseCase.selectAllPromotions(pageableModelMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> promotionController.getAllPromotions(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(promotionInConverter, never()).toPromotionsPage(any(), any());
  }

  @Test
  @DisplayName("Test controlador insertIntoPromotion Ok")
  void insertIntoPromotionOkTest() {

    // INPUT
    InsertPromotionDTO insertPromotionDTOInput = crearInsertPromotionDTO("Papelería");

    // ESPERADO
    PromotionDTO dtoEsperado = crearPromotionDTO(3, "Papelería");

    ResponseEntity<PromotionDTO> resEsperado =
        ResponseEntity.status(HttpStatus.CREATED).body(dtoEsperado);

    // MOCKS
    Promotion promotionNuevoMock = crearPromotion(null, "Papelería");
    Promotion promotionCreadoMock = crearPromotion(3, "Papelería");

    when(promotionInConverter.toDomainInsert(insertPromotionDTOInput))
        .thenReturn(promotionNuevoMock);
    when(newPromotionUseCase.newPromotion(promotionNuevoMock)).thenReturn(promotionCreadoMock);
    when(promotionInConverter.toDto(promotionCreadoMock))
        .thenReturn(crearPromotionDTO(3, "Papelería"));

    // EJECUCIÓN
    ResponseEntity<PromotionDTO> res =
        promotionController.insertIntoPromotion(insertPromotionDTOInput);

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
  @DisplayName("Test controlador insertIntoPromotion Ko")
  void insertIntoPromotionKoTest() {

    // INPUT
    InsertPromotionDTO insertPromotionDTOInput = crearInsertPromotionDTO("Papelería");

    // MOCKS
    Promotion promotionNuevoMock = crearPromotion(null, "Papelería");

    when(promotionInConverter.toDomainInsert(insertPromotionDTOInput))
        .thenReturn(promotionNuevoMock);
    when(newPromotionUseCase.newPromotion(promotionNuevoMock))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> promotionController.insertIntoPromotion(insertPromotionDTOInput));

    assertNotNull(ex.getMessage());
    verify(promotionInConverter, never()).toDto(any(Promotion.class));
  }

  @Test
  @DisplayName("Test controlador getPromotionById Ok")
  void getPromotionByIdOkTest() {

    // INPUT
    Integer promotionIdInput = 1;

    // ESPERADO
    PromotionDTO dtoEsperado = crearPromotionDTO(1, "Papelería");

    ResponseEntity<PromotionDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    Promotion promotionMock = crearPromotion(1, "Papelería");

    when(selectPromotionByIdUseCase.selectPromotionById(promotionIdInput))
        .thenReturn(promotionMock);
    when(promotionInConverter.toDto(promotionMock)).thenReturn(crearPromotionDTO(1, "Papelería"));

    // EJECUCIÓN
    ResponseEntity<PromotionDTO> res = promotionController.getPromotionById(promotionIdInput);

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
  @DisplayName("Test controlador getPromotionById Ko")
  void getPromotionByIdKoTest() {

    // INPUT
    Integer promotionIdInput = 99;

    // MOCKS
    when(selectPromotionByIdUseCase.selectPromotionById(promotionIdInput))
        .thenThrow(new PromotionNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    PromotionNotFoundException ex =
        assertThrows(
            PromotionNotFoundException.class,
            () -> promotionController.getPromotionById(promotionIdInput));

    assertEquals("No se encuentra la promoción con id: 99", ex.getMessage());
    verify(promotionInConverter, never()).toDto(any(Promotion.class));
  }

  @Test
  @DisplayName("Test controlador updatePromotion Ok")
  void updatePromotionOkTest() {

    // INPUT
    Integer promotionIdInput = 1;
    InsertPromotionDTO insertPromotionDTOInput = crearInsertPromotionDTO("Descuento actualizado");

    // ESPERADO
    PromotionDTO dtoEsperado = crearPromotionDTO(1, "Descuento actualizado");

    ResponseEntity<PromotionDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    Promotion promotionNuevosDatosMock = crearPromotion(null, "Descuento actualizado");
    Promotion promotionActualizadoMock = crearPromotion(1, "Descuento actualizado");

    when(promotionInConverter.toDomainInsert(insertPromotionDTOInput))
        .thenReturn(promotionNuevosDatosMock);
    when(updatePromotionUseCase.updatePromotion(promotionIdInput, promotionNuevosDatosMock))
        .thenReturn(promotionActualizadoMock);
    when(promotionInConverter.toDto(promotionActualizadoMock))
        .thenReturn(crearPromotionDTO(1, "Descuento actualizado"));

    // EJECUCIÓN
    ResponseEntity<PromotionDTO> res =
        promotionController.updatePromotion(promotionIdInput, insertPromotionDTOInput);

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
  @DisplayName("Test controlador updatePromotion Ko")
  void updatePromotionKoTest() {

    // INPUT
    Integer promotionIdInput = 99;
    InsertPromotionDTO insertPromotionDTOInput = crearInsertPromotionDTO("Descuento actualizado");

    // MOCKS
    Promotion promotionNuevosDatosMock = crearPromotion(null, "Descuento actualizado");

    when(promotionInConverter.toDomainInsert(insertPromotionDTOInput))
        .thenReturn(promotionNuevosDatosMock);
    when(updatePromotionUseCase.updatePromotion(promotionIdInput, promotionNuevosDatosMock))
        .thenThrow(new PromotionNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    PromotionNotFoundException ex =
        assertThrows(
            PromotionNotFoundException.class,
            () -> promotionController.updatePromotion(promotionIdInput, insertPromotionDTOInput));

    assertEquals("No se encuentra la promoción con id: 99", ex.getMessage());
    verify(promotionInConverter, never()).toDto(any(Promotion.class));
  }

  @Test
  @DisplayName("Test controlador deletePromotion Ok")
  void deletePromotionOkTest() {

    // INPUT
    Integer promotionIdInput = 1;

    // ESPERADO
    ResponseEntity<Void> resEsperado = ResponseEntity.noContent().build();

    // MOCKS
    doNothing().when(deletePromotionUseCase).deletePromotion(promotionIdInput);

    // EJECUCIÓN
    ResponseEntity<Void> res = promotionController.deletePromotion(promotionIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
          assertNull(res.getBody());
          assertEquals(resEsperado, res);
        });
    verify(deletePromotionUseCase).deletePromotion(promotionIdInput);
  }

  @Test
  @DisplayName("Test controlador deletePromotion Ko")
  void deletePromotionKoTest() {

    // INPUT
    Integer promotionIdInput = 99;

    // MOCKS
    doThrow(new PromotionNotFoundException(99))
        .when(deletePromotionUseCase)
        .deletePromotion(promotionIdInput);

    // EJECUCIÓN Y VALIDACIONES
    PromotionNotFoundException ex =
        assertThrows(
            PromotionNotFoundException.class,
            () -> promotionController.deletePromotion(promotionIdInput));

    assertEquals("No se encuentra la promoción con id: 99", ex.getMessage());
  }
}
