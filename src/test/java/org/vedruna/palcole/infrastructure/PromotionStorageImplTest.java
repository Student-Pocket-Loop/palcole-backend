package org.vedruna.palcole.infrastructure;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import org.vedruna.palcole.domain.exception.promotions.PromotionNotFoundException;
import org.vedruna.palcole.domain.model.Promotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.PromotionOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.CategoryEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.PromotionEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.CategoryRepository;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.PromotionRepository;

@ExtendWith(MockitoExtension.class)
class PromotionStorageImplTest {

  @Mock private PromotionRepository promotionRepository;

  @Mock private PromotionOutConverter promotionOutConverter;

  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private PromotionStorageImpl promotionStorageImpl;

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

  private PromotionEntity crearPromotionEntity(Integer promotionId, String title) {
    PromotionEntity o = new PromotionEntity();
    o.setPromotionId(promotionId);
    CategoryEntity categoryRef = new CategoryEntity();
    categoryRef.setCategoryId(1);
    o.setCategory(categoryRef);
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
  @DisplayName("Test storage selectAllPromotions Ok")
  void selectAllPromotionsOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<Promotion> pageModelEsperado =
        new PageModel<>(
            List.of(crearPromotion(1, "Papelería"), crearPromotion(2, "Mochilas")), 2, 1, 2, 10, 0);

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);
    Page<PromotionEntity> pageEntityMock =
        new PageImpl<>(
            List.of(crearPromotionEntity(1, "Papelería"), crearPromotionEntity(2, "Mochilas")),
            pageableMock,
            2);
    PageModel<Promotion> pageModelMock =
        new PageModel<>(
            List.of(crearPromotion(1, "Papelería"), crearPromotion(2, "Mochilas")), 2, 1, 2, 10, 0);

    when(promotionOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(promotionRepository.findAll(pageableMock)).thenReturn(pageEntityMock);
    when(promotionOutConverter.toPromotionPageModel(pageEntityMock)).thenReturn(pageModelMock);

    // EJECUCIÓN
    PageModel<Promotion> res = promotionStorageImpl.selectAllPromotions(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage selectAllPromotions Ko")
  void selectAllPromotionsKoTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "INVALID_SORT");

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);

    when(promotionOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(promotionRepository.findAll(pageableMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> promotionStorageImpl.selectAllPromotions(pageableModelInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(promotionOutConverter, never()).toPromotionPageModel(any());
  }

  @Test
  @DisplayName("Test storage findPromotionById Ok")
  void findPromotionByIdOkTest() {

    // INPUT
    Integer promotionIdInput = 1;

    // ESPERADO
    Promotion promotionEsperado = crearPromotion(1, "Papelería");

    // MOCKS
    PromotionEntity promotionEntityMock = crearPromotionEntity(1, "Papelería");
    Promotion promotionMock = crearPromotion(1, "Papelería");

    when(promotionRepository.findById(promotionIdInput))
        .thenReturn(Optional.of(promotionEntityMock));
    when(promotionOutConverter.toDomain(promotionEntityMock)).thenReturn(promotionMock);

    // EJECUCIÓN
    Promotion res = promotionStorageImpl.findPromotionById(promotionIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(promotionEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage findPromotionById Ko")
  void findPromotionByIdKoTest() {

    // INPUT
    Integer promotionIdInput = 99;

    // MOCKS
    when(promotionRepository.findById(promotionIdInput)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    PromotionNotFoundException ex =
        assertThrows(
            PromotionNotFoundException.class,
            () -> promotionStorageImpl.findPromotionById(promotionIdInput));

    assertEquals("No se encuentra la promoción con id: 99", ex.getMessage());
    verify(promotionOutConverter, never()).toDomain(any(PromotionEntity.class));
  }

  @Test
  @DisplayName("Test storage updatePromotion Ok")
  void updatePromotionOkTest() {

    // INPUT
    Integer promotionIdInput = 1;
    Promotion newPromotionInput = crearPromotion(null, "Descuento actualizado");

    // ESPERADO
    Promotion promotionEsperado = crearPromotion(1, "Descuento actualizado");

    // MOCKS
    PromotionEntity promotionEntityExistenteMock = crearPromotionEntity(1, "Papelería");
    PromotionEntity promotionEntityGuardadoMock = crearPromotionEntity(1, "Descuento actualizado");
    Promotion promotionActualizadoMock = crearPromotion(1, "Descuento actualizado");

    CategoryEntity categoryEntityMock = new CategoryEntity();
    when(promotionRepository.findById(promotionIdInput))
        .thenReturn(Optional.of(promotionEntityExistenteMock));
    when(categoryRepository.findById(1)).thenReturn(Optional.of(categoryEntityMock));
    when(promotionRepository.save(promotionEntityExistenteMock))
        .thenReturn(promotionEntityGuardadoMock);
    when(promotionOutConverter.toDomain(promotionEntityGuardadoMock))
        .thenReturn(promotionActualizadoMock);

    // EJECUCIÓN
    Promotion res = promotionStorageImpl.updatePromotion(promotionIdInput, newPromotionInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals("Descuento actualizado", promotionEntityExistenteMock.getTitle());
          assertEquals(categoryEntityMock, promotionEntityExistenteMock.getCategory());
          assertEquals(promotionEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage updatePromotion Ko")
  void updatePromotionKoTest() {

    // INPUT
    Integer promotionIdInput = 99;
    Promotion newPromotionInput = crearPromotion(null, "Descuento actualizado");

    // MOCKS
    when(promotionRepository.findById(promotionIdInput)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    PromotionNotFoundException ex =
        assertThrows(
            PromotionNotFoundException.class,
            () -> promotionStorageImpl.updatePromotion(promotionIdInput, newPromotionInput));

    assertEquals("No se encuentra la promoción con id: 99", ex.getMessage());
    verify(promotionRepository, never()).save(any(PromotionEntity.class));
    verify(promotionOutConverter, never()).toDomain(any(PromotionEntity.class));
  }

  @Test
  @DisplayName("Test storage updatePromotion Ko (category inexistente)")
  void updatePromotionKoCategoryNotFoundTest() {

    // INPUT
    Integer promotionIdInput = 1;
    Promotion newPromotionInput = crearPromotion(null, "Descuento actualizado");

    // MOCKS
    PromotionEntity promotionEntityExistenteMock = crearPromotionEntity(1, "Papelería");

    when(promotionRepository.findById(promotionIdInput))
        .thenReturn(Optional.of(promotionEntityExistenteMock));
    when(categoryRepository.findById(1)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    CategoryNotFoundException ex =
        assertThrows(
            CategoryNotFoundException.class,
            () -> promotionStorageImpl.updatePromotion(promotionIdInput, newPromotionInput));

    assertNotNull(ex.getMessage());
    verify(promotionRepository, never()).save(any(PromotionEntity.class));
  }

  @Test
  @DisplayName("Test storage insertPromotion Ok")
  void insertPromotionOkTest() {

    // INPUT
    Promotion newPromotionInput = crearPromotion(null, "Papelería");

    // ESPERADO
    Promotion promotionEsperado = crearPromotion(3, "Papelería");

    // MOCKS
    PromotionEntity promotionEntityNuevoMock = crearPromotionEntity(null, "Papelería");
    PromotionEntity promotionEntityGuardadoMock = crearPromotionEntity(3, "Papelería");
    Promotion promotionCreadoMock = crearPromotion(3, "Papelería");

    CategoryEntity categoryEntityMock = new CategoryEntity();
    when(promotionOutConverter.toEntity(newPromotionInput)).thenReturn(promotionEntityNuevoMock);
    when(categoryRepository.findById(1)).thenReturn(Optional.of(categoryEntityMock));
    when(promotionRepository.save(promotionEntityNuevoMock))
        .thenReturn(promotionEntityGuardadoMock);
    when(promotionOutConverter.toDomain(promotionEntityGuardadoMock))
        .thenReturn(promotionCreadoMock);

    // EJECUCIÓN
    Promotion res = promotionStorageImpl.insertPromotion(newPromotionInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(promotionEsperado, res);
          assertEquals(categoryEntityMock, promotionEntityNuevoMock.getCategory());
        });
  }

  @Test
  @DisplayName("Test storage insertPromotion Ko")
  void insertPromotionKoTest() {

    // INPUT
    Promotion newPromotionInput = crearPromotion(null, "Papelería");

    // MOCKS
    PromotionEntity promotionEntityNuevoMock = crearPromotionEntity(null, "Papelería");

    CategoryEntity categoryEntityMock = new CategoryEntity();
    when(promotionOutConverter.toEntity(newPromotionInput)).thenReturn(promotionEntityNuevoMock);
    when(categoryRepository.findById(1)).thenReturn(Optional.of(categoryEntityMock));
    when(promotionRepository.save(promotionEntityNuevoMock))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> promotionStorageImpl.insertPromotion(newPromotionInput));

    assertEquals("Conflicto con los datos existentes", ex.getMessage());
    verify(promotionOutConverter, never()).toDomain(any(PromotionEntity.class));
  }

  @Test
  @DisplayName("Test storage insertPromotion Ko (category inexistente)")
  void insertPromotionKoCategoryNotFoundTest() {

    // INPUT
    Promotion newPromotionInput = crearPromotion(null, "Papelería");

    // MOCKS
    PromotionEntity promotionEntityNuevoMock = crearPromotionEntity(null, "Papelería");

    when(promotionOutConverter.toEntity(newPromotionInput)).thenReturn(promotionEntityNuevoMock);
    when(categoryRepository.findById(1)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    CategoryNotFoundException ex =
        assertThrows(
            CategoryNotFoundException.class,
            () -> promotionStorageImpl.insertPromotion(newPromotionInput));

    assertNotNull(ex.getMessage());
    verify(promotionRepository, never()).save(any(PromotionEntity.class));
  }

  @Test
  @DisplayName("Test storage deletePromotion Ok")
  void deletePromotionOkTest() {

    // INPUT
    Integer promotionIdInput = 1;

    // MOCKS
    when(promotionRepository.existsById(promotionIdInput)).thenReturn(true);
    doNothing().when(promotionRepository).deleteById(promotionIdInput);

    // EJECUCIÓN
    promotionStorageImpl.deletePromotion(promotionIdInput);

    // VALIDACIONES
    verify(promotionRepository).existsById(promotionIdInput);
    verify(promotionRepository).deleteById(promotionIdInput);
  }

  @Test
  @DisplayName("Test storage deletePromotion Ko")
  void deletePromotionKoTest() {

    // INPUT
    Integer promotionIdInput = 99;

    // MOCKS
    when(promotionRepository.existsById(promotionIdInput)).thenReturn(false);

    // EJECUCIÓN Y VALIDACIONES
    PromotionNotFoundException ex =
        assertThrows(
            PromotionNotFoundException.class,
            () -> promotionStorageImpl.deletePromotion(promotionIdInput));

    assertEquals("No se encuentra la promoción con id: 99", ex.getMessage());
    verify(promotionRepository, never()).deleteById(any());
  }
}
