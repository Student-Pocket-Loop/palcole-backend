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
import org.vedruna.palcole.domain.exception.conversations.ConversationNotFoundException;
import org.vedruna.palcole.domain.exception.users.UserNotFoundException;
import org.vedruna.palcole.domain.model.Conversation;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.ConversationOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.AcademicItemEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.ConversationEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.AcademicItemRepository;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.ConversationRepository;
import org.vedruna.palcole.persistance.model.UserEntity;
import org.vedruna.palcole.persistance.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ConversationStorageImplTest {

  @Mock private ConversationRepository conversationRepository;

  @Mock private ConversationOutConverter conversationOutConverter;

  @Mock private AcademicItemRepository academicItemRepository;

  @Mock private UserRepository userRepository;

  @InjectMocks private ConversationStorageImpl conversationStorageImpl;

  private Conversation crearConversation(Integer conversationId, Integer itemId) {
    Conversation o = new Conversation();
    o.setConversationId(conversationId);
    o.setItemId(itemId);
    o.setBuyerId(2);
    o.setStartedAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  private ConversationEntity crearConversationEntity(Integer conversationId, Integer itemId) {
    ConversationEntity o = new ConversationEntity();
    o.setConversationId(conversationId);
    AcademicItemEntity itemRef = new AcademicItemEntity();
    itemRef.setItemId(itemId);
    o.setItem(itemRef);
    UserEntity buyerRef = new UserEntity();
    buyerRef.setUserId(2);
    o.setBuyer(buyerRef);
    o.setStartedAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  @Test
  @DisplayName("Test storage selectAllConversations Ok")
  void selectAllConversationsOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<Conversation> pageModelEsperado =
        new PageModel<>(
            List.of(crearConversation(1, 10), crearConversation(2, 20)), 2, 1, 2, 10, 0);

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);
    Page<ConversationEntity> pageEntityMock =
        new PageImpl<>(
            List.of(crearConversationEntity(1, 10), crearConversationEntity(2, 20)),
            pageableMock,
            2);
    PageModel<Conversation> pageModelMock =
        new PageModel<>(
            List.of(crearConversation(1, 10), crearConversation(2, 20)), 2, 1, 2, 10, 0);

    when(conversationOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(conversationRepository.findAll(pageableMock)).thenReturn(pageEntityMock);
    when(conversationOutConverter.toConversationPageModel(pageEntityMock))
        .thenReturn(pageModelMock);

    // EJECUCIÓN
    PageModel<Conversation> res =
        conversationStorageImpl.selectAllConversations(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage selectAllConversations Ko")
  void selectAllConversationsKoTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "INVALID_SORT");

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);

    when(conversationOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(conversationRepository.findAll(pageableMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> conversationStorageImpl.selectAllConversations(pageableModelInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(conversationOutConverter, never()).toConversationPageModel(any());
  }

  @Test
  @DisplayName("Test storage findConversationById Ok")
  void findConversationByIdOkTest() {

    // INPUT
    Integer conversationIdInput = 1;

    // ESPERADO
    Conversation conversationEsperado = crearConversation(1, 10);

    // MOCKS
    ConversationEntity conversationEntityMock = crearConversationEntity(1, 10);
    Conversation conversationMock = crearConversation(1, 10);

    when(conversationRepository.findById(conversationIdInput))
        .thenReturn(Optional.of(conversationEntityMock));
    when(conversationOutConverter.toDomain(conversationEntityMock)).thenReturn(conversationMock);

    // EJECUCIÓN
    Conversation res = conversationStorageImpl.findConversationById(conversationIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(conversationEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage findConversationById Ko")
  void findConversationByIdKoTest() {

    // INPUT
    Integer conversationIdInput = 99;

    // MOCKS
    when(conversationRepository.findById(conversationIdInput)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    ConversationNotFoundException ex =
        assertThrows(
            ConversationNotFoundException.class,
            () -> conversationStorageImpl.findConversationById(conversationIdInput));

    assertEquals("No se encuentra la conversación con id: 99", ex.getMessage());
    verify(conversationOutConverter, never()).toDomain(any(ConversationEntity.class));
  }

  @Test
  @DisplayName("Test storage insertConversation Ok")
  void insertConversationOkTest() {

    // INPUT
    Conversation newConversationInput = crearConversation(null, 10);

    // ESPERADO
    Conversation conversationEsperado = crearConversation(3, 10);

    // MOCKS
    ConversationEntity conversationEntityNuevoMock = crearConversationEntity(null, 10);
    ConversationEntity conversationEntityGuardadoMock = crearConversationEntity(3, 10);
    Conversation conversationCreadoMock = crearConversation(3, 10);

    AcademicItemEntity itemEntityMock = new AcademicItemEntity();
    UserEntity buyerEntityMock = new UserEntity();
    when(conversationOutConverter.toEntity(newConversationInput))
        .thenReturn(conversationEntityNuevoMock);
    when(academicItemRepository.findById(10)).thenReturn(Optional.of(itemEntityMock));
    when(userRepository.findById(2)).thenReturn(Optional.of(buyerEntityMock));
    when(conversationRepository.save(conversationEntityNuevoMock))
        .thenReturn(conversationEntityGuardadoMock);
    when(conversationOutConverter.toDomain(conversationEntityGuardadoMock))
        .thenReturn(conversationCreadoMock);

    // EJECUCIÓN
    Conversation res = conversationStorageImpl.insertConversation(newConversationInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(conversationEsperado, res);
          assertEquals(itemEntityMock, conversationEntityNuevoMock.getItem());
          assertEquals(buyerEntityMock, conversationEntityNuevoMock.getBuyer());
        });
  }

  @Test
  @DisplayName("Test storage insertConversation Ko")
  void insertConversationKoTest() {

    // INPUT
    Conversation newConversationInput = crearConversation(null, 10);

    // MOCKS
    ConversationEntity conversationEntityNuevoMock = crearConversationEntity(null, 10);

    AcademicItemEntity itemEntityMock = new AcademicItemEntity();
    UserEntity buyerEntityMock = new UserEntity();
    when(conversationOutConverter.toEntity(newConversationInput))
        .thenReturn(conversationEntityNuevoMock);
    when(academicItemRepository.findById(10)).thenReturn(Optional.of(itemEntityMock));
    when(userRepository.findById(2)).thenReturn(Optional.of(buyerEntityMock));
    when(conversationRepository.save(conversationEntityNuevoMock))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> conversationStorageImpl.insertConversation(newConversationInput));

    assertEquals("Conflicto con los datos existentes", ex.getMessage());
    verify(conversationOutConverter, never()).toDomain(any(ConversationEntity.class));
  }

  @Test
  @DisplayName("Test storage insertConversation Ko (item inexistente)")
  void insertConversationKoItemNotFoundTest() {

    // INPUT
    Conversation newConversationInput = crearConversation(null, 10);

    // MOCKS
    ConversationEntity conversationEntityNuevoMock = crearConversationEntity(null, 10);

    when(conversationOutConverter.toEntity(newConversationInput))
        .thenReturn(conversationEntityNuevoMock);
    when(academicItemRepository.findById(10)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    AcademicItemNotFoundException ex =
        assertThrows(
            AcademicItemNotFoundException.class,
            () -> conversationStorageImpl.insertConversation(newConversationInput));

    assertNotNull(ex.getMessage());
    verify(conversationRepository, never()).save(any(ConversationEntity.class));
  }

  @Test
  @DisplayName("Test storage insertConversation Ko (buyer inexistente)")
  void insertConversationKoBuyerNotFoundTest() {

    // INPUT
    Conversation newConversationInput = crearConversation(null, 10);

    // MOCKS
    ConversationEntity conversationEntityNuevoMock = crearConversationEntity(null, 10);

    AcademicItemEntity itemEntityMock = new AcademicItemEntity();
    when(conversationOutConverter.toEntity(newConversationInput))
        .thenReturn(conversationEntityNuevoMock);
    when(academicItemRepository.findById(10)).thenReturn(Optional.of(itemEntityMock));
    when(userRepository.findById(2)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    UserNotFoundException ex =
        assertThrows(
            UserNotFoundException.class,
            () -> conversationStorageImpl.insertConversation(newConversationInput));

    assertNotNull(ex.getMessage());
    verify(conversationRepository, never()).save(any(ConversationEntity.class));
  }

  @Test
  @DisplayName("Test storage deleteConversation Ok")
  void deleteConversationOkTest() {

    // INPUT
    Integer conversationIdInput = 1;

    // MOCKS
    when(conversationRepository.existsById(conversationIdInput)).thenReturn(true);
    doNothing().when(conversationRepository).deleteById(conversationIdInput);

    // EJECUCIÓN
    conversationStorageImpl.deleteConversation(conversationIdInput);

    // VALIDACIONES
    verify(conversationRepository).existsById(conversationIdInput);
    verify(conversationRepository).deleteById(conversationIdInput);
  }

  @Test
  @DisplayName("Test storage deleteConversation Ko")
  void deleteConversationKoTest() {

    // INPUT
    Integer conversationIdInput = 99;

    // MOCKS
    when(conversationRepository.existsById(conversationIdInput)).thenReturn(false);

    // EJECUCIÓN Y VALIDACIONES
    ConversationNotFoundException ex =
        assertThrows(
            ConversationNotFoundException.class,
            () -> conversationStorageImpl.deleteConversation(conversationIdInput));

    assertEquals("No se encuentra la conversación con id: 99", ex.getMessage());
    verify(conversationRepository, never()).deleteById(any());
  }
}
