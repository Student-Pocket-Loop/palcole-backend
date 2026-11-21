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
import java.time.LocalDateTime;
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
import org.vedruna.palcole.domain.exception.academicItems.AcademicItemNotFoundException;
import org.vedruna.palcole.domain.model.AcademicItem;
import org.vedruna.palcole.domain.model.enums.ItemCondition;
import org.vedruna.palcole.domain.model.enums.ListingType;
import org.vedruna.palcole.domain.model.enums.SaleStatus;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.AcademicItemOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.AcademicItemEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.AcademicItemRepository;

@ExtendWith(MockitoExtension.class)
class AcademicItemStorageImplTest {

  @Mock private AcademicItemRepository academicItemRepository;

  @Mock private AcademicItemOutConverter academicItemOutConverter;

  @InjectMocks private AcademicItemStorageImpl academicItemStorageImpl;

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

  @Test
  @DisplayName("Test storage selectAllAcademicItems Ok")
  void selectAllAcademicItemsOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

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

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);
    Page<AcademicItemEntity> pageEntityMock =
        new PageImpl<>(
            List.of(
                crearAcademicItemEntity(1, "Calculadora"),
                crearAcademicItemEntity(2, "Libro de Matemáticas")),
            pageableMock,
            2);
    PageModel<AcademicItem> pageModelMock =
        new PageModel<>(
            List.of(
                crearAcademicItem(1, "Calculadora"), crearAcademicItem(2, "Libro de Matemáticas")),
            2,
            1,
            2,
            10,
            0);

    when(academicItemOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(academicItemRepository.findAll(pageableMock)).thenReturn(pageEntityMock);
    when(academicItemOutConverter.toAcademicItemPageModel(pageEntityMock))
        .thenReturn(pageModelMock);

    // EJECUCIÓN
    PageModel<AcademicItem> res =
        academicItemStorageImpl.selectAllAcademicItems(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage selectAllAcademicItems Ko")
  void selectAllAcademicItemsKoTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "INVALID_SORT");

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);

    when(academicItemOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(academicItemRepository.findAll(pageableMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> academicItemStorageImpl.selectAllAcademicItems(pageableModelInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(academicItemOutConverter, never()).toAcademicItemPageModel(any());
  }

  @Test
  @DisplayName("Test storage findAcademicItemById Ok")
  void findAcademicItemByIdOkTest() {

    // INPUT
    Integer itemIdInput = 1;

    // ESPERADO
    AcademicItem academicItemEsperado = crearAcademicItem(1, "Calculadora");

    // MOCKS
    AcademicItemEntity academicItemEntityMock = crearAcademicItemEntity(1, "Calculadora");
    AcademicItem academicItemMock = crearAcademicItem(1, "Calculadora");

    when(academicItemRepository.findById(itemIdInput))
        .thenReturn(Optional.of(academicItemEntityMock));
    when(academicItemOutConverter.toDomain(academicItemEntityMock)).thenReturn(academicItemMock);

    // EJECUCIÓN
    AcademicItem res = academicItemStorageImpl.findAcademicItemById(itemIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(academicItemEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage findAcademicItemById Ko")
  void findAcademicItemByIdKoTest() {

    // INPUT
    Integer itemIdInput = 99;

    // MOCKS
    when(academicItemRepository.findById(itemIdInput)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    AcademicItemNotFoundException ex =
        assertThrows(
            AcademicItemNotFoundException.class,
            () -> academicItemStorageImpl.findAcademicItemById(itemIdInput));

    assertEquals("No se encuentra el artículo con id: 99", ex.getMessage());
    verify(academicItemOutConverter, never()).toDomain(any(AcademicItemEntity.class));
  }

  @Test
  @DisplayName("Test storage updateAcademicItem Ok")
  void updateAcademicItemOkTest() {

    // INPUT
    Integer itemIdInput = 1;
    AcademicItem newAcademicItemInput = crearAcademicItem(null, "Calculadora actualizada");

    // ESPERADO
    AcademicItem academicItemEsperado = crearAcademicItem(1, "Calculadora actualizada");

    // MOCKS
    AcademicItemEntity academicItemEntityExistenteMock = crearAcademicItemEntity(1, "Calculadora");
    AcademicItemEntity academicItemEntityGuardadoMock =
        crearAcademicItemEntity(1, "Calculadora actualizada");
    AcademicItem academicItemActualizadoMock = crearAcademicItem(1, "Calculadora actualizada");

    when(academicItemRepository.findById(itemIdInput))
        .thenReturn(Optional.of(academicItemEntityExistenteMock));
    when(academicItemRepository.save(academicItemEntityExistenteMock))
        .thenReturn(academicItemEntityGuardadoMock);
    when(academicItemOutConverter.toDomain(academicItemEntityGuardadoMock))
        .thenReturn(academicItemActualizadoMock);

    // EJECUCIÓN
    AcademicItem res =
        academicItemStorageImpl.updateAcademicItem(itemIdInput, newAcademicItemInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals("Calculadora actualizada", academicItemEntityExistenteMock.getTitle());
          assertEquals(academicItemEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage updateAcademicItem conserva los opcionales que llegan nulos")
  void updateAcademicItemOpcionalesNulosTest() {

    // INPUT
    Integer itemIdInput = 1;
    AcademicItem newAcademicItemInput = crearAcademicItem(null, "Calculadora actualizada");
    newAcademicItemInput.setPrice(null);
    newAcademicItemInput.setListingType(null);
    newAcademicItemInput.setSaleStatus(null);

    // MOCKS
    AcademicItemEntity academicItemEntityExistenteMock = crearAcademicItemEntity(1, "Calculadora");
    AcademicItemEntity academicItemEntityGuardadoMock =
        crearAcademicItemEntity(1, "Calculadora actualizada");
    AcademicItem academicItemActualizadoMock = crearAcademicItem(1, "Calculadora actualizada");

    when(academicItemRepository.findById(itemIdInput))
        .thenReturn(Optional.of(academicItemEntityExistenteMock));
    when(academicItemRepository.save(academicItemEntityExistenteMock))
        .thenReturn(academicItemEntityGuardadoMock);
    when(academicItemOutConverter.toDomain(academicItemEntityGuardadoMock))
        .thenReturn(academicItemActualizadoMock);

    // EJECUCIÓN
    AcademicItem res =
        academicItemStorageImpl.updateAcademicItem(itemIdInput, newAcademicItemInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(new BigDecimal("10.00"), academicItemEntityExistenteMock.getPrice());
          assertEquals(ListingType.VENTA, academicItemEntityExistenteMock.getListingType());
          assertEquals(SaleStatus.DISPONIBLE, academicItemEntityExistenteMock.getSaleStatus());
        });
  }

  @Test
  @DisplayName("Test storage updateAcademicItem Ko")
  void updateAcademicItemKoTest() {

    // INPUT
    Integer itemIdInput = 99;
    AcademicItem newAcademicItemInput = crearAcademicItem(null, "Calculadora actualizada");

    // MOCKS
    when(academicItemRepository.findById(itemIdInput)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    AcademicItemNotFoundException ex =
        assertThrows(
            AcademicItemNotFoundException.class,
            () -> academicItemStorageImpl.updateAcademicItem(itemIdInput, newAcademicItemInput));

    assertEquals("No se encuentra el artículo con id: 99", ex.getMessage());
    verify(academicItemRepository, never()).save(any(AcademicItemEntity.class));
    verify(academicItemOutConverter, never()).toDomain(any(AcademicItemEntity.class));
  }

  @Test
  @DisplayName("Test storage insertAcademicItem Ok")
  void insertAcademicItemOkTest() {

    // INPUT
    AcademicItem newAcademicItemInput = crearAcademicItem(null, "Calculadora");

    // ESPERADO
    AcademicItem academicItemEsperado = crearAcademicItem(3, "Calculadora");

    // MOCKS
    AcademicItemEntity academicItemEntityNuevoMock = crearAcademicItemEntity(null, "Calculadora");
    AcademicItemEntity academicItemEntityGuardadoMock = crearAcademicItemEntity(3, "Calculadora");
    AcademicItem academicItemCreadoMock = crearAcademicItem(3, "Calculadora");

    when(academicItemOutConverter.toEntity(newAcademicItemInput))
        .thenReturn(academicItemEntityNuevoMock);
    when(academicItemRepository.save(academicItemEntityNuevoMock))
        .thenReturn(academicItemEntityGuardadoMock);
    when(academicItemOutConverter.toDomain(academicItemEntityGuardadoMock))
        .thenReturn(academicItemCreadoMock);

    // EJECUCIÓN
    AcademicItem res = academicItemStorageImpl.insertAcademicItem(newAcademicItemInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(academicItemEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage insertAcademicItem Ko")
  void insertAcademicItemKoTest() {

    // INPUT
    AcademicItem newAcademicItemInput = crearAcademicItem(null, "Calculadora");

    // MOCKS
    AcademicItemEntity academicItemEntityNuevoMock = crearAcademicItemEntity(null, "Calculadora");

    when(academicItemOutConverter.toEntity(newAcademicItemInput))
        .thenReturn(academicItemEntityNuevoMock);
    when(academicItemRepository.save(academicItemEntityNuevoMock))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> academicItemStorageImpl.insertAcademicItem(newAcademicItemInput));

    assertEquals("Conflicto con los datos existentes", ex.getMessage());
    verify(academicItemOutConverter, never()).toDomain(any(AcademicItemEntity.class));
  }

  @Test
  @DisplayName("Test storage deleteAcademicItem Ok")
  void deleteAcademicItemOkTest() {

    // INPUT
    Integer itemIdInput = 1;

    // MOCKS
    when(academicItemRepository.existsById(itemIdInput)).thenReturn(true);
    doNothing().when(academicItemRepository).deleteById(itemIdInput);

    // EJECUCIÓN
    academicItemStorageImpl.deleteAcademicItem(itemIdInput);

    // VALIDACIONES
    verify(academicItemRepository).existsById(itemIdInput);
    verify(academicItemRepository).deleteById(itemIdInput);
  }

  @Test
  @DisplayName("Test storage deleteAcademicItem Ko")
  void deleteAcademicItemKoTest() {

    // INPUT
    Integer itemIdInput = 99;

    // MOCKS
    when(academicItemRepository.existsById(itemIdInput)).thenReturn(false);

    // EJECUCIÓN Y VALIDACIONES
    AcademicItemNotFoundException ex =
        assertThrows(
            AcademicItemNotFoundException.class,
            () -> academicItemStorageImpl.deleteAcademicItem(itemIdInput));

    assertEquals("No se encuentra el artículo con id: 99", ex.getMessage());
    verify(academicItemRepository, never()).deleteById(any());
  }
}
