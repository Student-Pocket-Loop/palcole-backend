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
import org.vedruna.palcole.domain.exception.conversations.ConversationNotFoundException;
import org.vedruna.palcole.domain.model.Conversation;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.conversations.DeleteConversationUseCase;
import org.vedruna.palcole.domain.ports.inbound.conversations.NewConversationUseCase;
import org.vedruna.palcole.domain.ports.inbound.conversations.SelectAllConversationsUseCase;
import org.vedruna.palcole.domain.ports.inbound.conversations.SelectConversationByIdUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.ConversationInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.conversations.ConversationDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.conversations.InsertConversationDTO;

@ExtendWith(MockitoExtension.class)
class ConversationControllerTest {

  @Mock private NewConversationUseCase newConversationUseCase;

  @Mock private DeleteConversationUseCase deleteConversationUseCase;

  @Mock private SelectAllConversationsUseCase selectAllConversationsUseCase;

  @Mock private SelectConversationByIdUseCase selectConversationByIdUseCase;

  @Mock private ConversationInConverter conversationInConverter;

  @InjectMocks private ConversationController conversationController;

  private Conversation crearConversation(Integer conversationId, Integer itemId) {
    Conversation o = new Conversation();
    o.setConversationId(conversationId);
    o.setItemId(itemId);
    o.setBuyerId(2);
    o.setStartedAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  private ConversationDTO crearConversationDTO(Integer conversationId, Integer itemId) {
    ConversationDTO o = new ConversationDTO();
    o.setConversationId(conversationId);
    o.setItemId(itemId);
    o.setBuyerId(2);
    o.setStartedAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  private InsertConversationDTO crearInsertConversationDTO(Integer itemId) {
    InsertConversationDTO o = new InsertConversationDTO();
    o.setItemId(itemId);
    o.setBuyerId(2);
    return o;
  }

  @Test
  @DisplayName("Test controlador getAll Ok")
  void getAllOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10);

    // ESPERADO
    List<ConversationDTO> lista = List.of(crearConversationDTO(1, 10), crearConversationDTO(2, 20));

    Pageable pageableEsperado = PageRequest.of(0, 10);

    Page<ConversationDTO> pageEsperado = new PageImpl<>(lista, pageableEsperado, 2);

    ResponseEntity<Page<ConversationDTO>> resEsperado = ResponseEntity.ok(pageEsperado);

    // MOCKS
    // CONVERTER MOCK (Pageable -> PageableModel)
    PageableModel pageableModelMock = new PageableModel(0, 10, "UNSORTED");

    when(conversationInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);

    // USE CASE MOCK
    PageModel<Conversation> pageModelMock =
        new PageModel<>(
            List.of(crearConversation(1, 10), crearConversation(2, 20)), 2, 1, 2, 10, 0);

    when(selectAllConversationsUseCase.selectAllConversations(pageableModelMock))
        .thenReturn(pageModelMock);

    // CONVERTER MOCK (PageModel -> Page de DTOs)
    Page<ConversationDTO> pageDtoMock =
        new PageImpl<>(
            List.of(crearConversationDTO(1, 10), crearConversationDTO(2, 20)),
            PageRequest.of(0, 10),
            2);

    when(conversationInConverter.toConversationsPage(pageModelMock, pageableModelMock))
        .thenReturn(pageDtoMock);

    // EJECUCIÓN
    ResponseEntity<Page<ConversationDTO>> res =
        conversationController.getAllConversations(pageableInput);

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

    when(conversationInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);
    when(selectAllConversationsUseCase.selectAllConversations(pageableModelMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> conversationController.getAllConversations(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(conversationInConverter, never()).toConversationsPage(any(), any());
  }

  @Test
  @DisplayName("Test controlador insertIntoConversation Ok")
  void insertIntoConversationOkTest() {

    // INPUT
    InsertConversationDTO insertConversationDTOInput = crearInsertConversationDTO(10);

    // ESPERADO
    ConversationDTO dtoEsperado = crearConversationDTO(3, 10);

    ResponseEntity<ConversationDTO> resEsperado =
        ResponseEntity.status(HttpStatus.CREATED).body(dtoEsperado);

    // MOCKS
    Conversation conversationNuevoMock = crearConversation(null, 10);
    Conversation conversationCreadoMock = crearConversation(3, 10);

    when(conversationInConverter.toDomainInsert(insertConversationDTOInput))
        .thenReturn(conversationNuevoMock);
    when(newConversationUseCase.newConversation(conversationNuevoMock))
        .thenReturn(conversationCreadoMock);
    when(conversationInConverter.toDto(conversationCreadoMock))
        .thenReturn(crearConversationDTO(3, 10));

    // EJECUCIÓN
    ResponseEntity<ConversationDTO> res =
        conversationController.insertIntoConversation(insertConversationDTOInput);

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
  @DisplayName("Test controlador insertIntoConversation Ko")
  void insertIntoConversationKoTest() {

    // INPUT
    InsertConversationDTO insertConversationDTOInput = crearInsertConversationDTO(10);

    // MOCKS
    Conversation conversationNuevoMock = crearConversation(null, 10);

    when(conversationInConverter.toDomainInsert(insertConversationDTOInput))
        .thenReturn(conversationNuevoMock);
    when(newConversationUseCase.newConversation(conversationNuevoMock))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> conversationController.insertIntoConversation(insertConversationDTOInput));

    assertNotNull(ex.getMessage());
    verify(conversationInConverter, never()).toDto(any(Conversation.class));
  }

  @Test
  @DisplayName("Test controlador getConversationById Ok")
  void getConversationByIdOkTest() {

    // INPUT
    Integer conversationIdInput = 1;

    // ESPERADO
    ConversationDTO dtoEsperado = crearConversationDTO(1, 10);

    ResponseEntity<ConversationDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    Conversation conversationMock = crearConversation(1, 10);

    when(selectConversationByIdUseCase.selectConversationById(conversationIdInput))
        .thenReturn(conversationMock);
    when(conversationInConverter.toDto(conversationMock)).thenReturn(crearConversationDTO(1, 10));

    // EJECUCIÓN
    ResponseEntity<ConversationDTO> res =
        conversationController.getConversationById(conversationIdInput);

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
  @DisplayName("Test controlador getConversationById Ko")
  void getConversationByIdKoTest() {

    // INPUT
    Integer conversationIdInput = 99;

    // MOCKS
    when(selectConversationByIdUseCase.selectConversationById(conversationIdInput))
        .thenThrow(new ConversationNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    ConversationNotFoundException ex =
        assertThrows(
            ConversationNotFoundException.class,
            () -> conversationController.getConversationById(conversationIdInput));

    assertEquals("No se encuentra la conversación con id: 99", ex.getMessage());
    verify(conversationInConverter, never()).toDto(any(Conversation.class));
  }

  @Test
  @DisplayName("Test controlador deleteConversation Ok")
  void deleteConversationOkTest() {

    // INPUT
    Integer conversationIdInput = 1;

    // ESPERADO
    ResponseEntity<Void> resEsperado = ResponseEntity.noContent().build();

    // MOCKS
    doNothing().when(deleteConversationUseCase).deleteConversation(conversationIdInput);

    // EJECUCIÓN
    ResponseEntity<Void> res = conversationController.deleteConversation(conversationIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
          assertNull(res.getBody());
          assertEquals(resEsperado, res);
        });
    verify(deleteConversationUseCase).deleteConversation(conversationIdInput);
  }

  @Test
  @DisplayName("Test controlador deleteConversation Ko")
  void deleteConversationKoTest() {

    // INPUT
    Integer conversationIdInput = 99;

    // MOCKS
    doThrow(new ConversationNotFoundException(99))
        .when(deleteConversationUseCase)
        .deleteConversation(conversationIdInput);

    // EJECUCIÓN Y VALIDACIONES
    ConversationNotFoundException ex =
        assertThrows(
            ConversationNotFoundException.class,
            () -> conversationController.deleteConversation(conversationIdInput));

    assertEquals("No se encuentra la conversación con id: 99", ex.getMessage());
  }
}
