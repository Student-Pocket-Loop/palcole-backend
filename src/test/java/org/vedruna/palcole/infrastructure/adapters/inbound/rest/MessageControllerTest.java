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
import org.vedruna.palcole.domain.exception.MessageNotFoundException;
import org.vedruna.palcole.domain.model.Message;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.messages.DeleteMessageUseCase;
import org.vedruna.palcole.domain.ports.inbound.messages.NewMessageUseCase;
import org.vedruna.palcole.domain.ports.inbound.messages.SelectAllMessagesUseCase;
import org.vedruna.palcole.domain.ports.inbound.messages.SelectMessageByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.messages.UpdateMessageUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.MessageInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.messages.InsertMessageDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.messages.MessageDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.messages.UpdateMessageDTO;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

  @Mock private NewMessageUseCase newMessageUseCase;

  @Mock private DeleteMessageUseCase deleteMessageUseCase;

  @Mock private SelectAllMessagesUseCase selectAllMessagesUseCase;

  @Mock private SelectMessageByIdUseCase selectMessageByIdUseCase;

  @Mock private UpdateMessageUseCase updateMessageUseCase;

  @Mock private MessageInConverter messageInConverter;

  @InjectMocks private MessageController messageController;

  private Message crearMessage(Integer messageId, String content) {
    Message o = new Message();
    o.setMessageId(messageId);
    o.setConversationId(1);
    o.setSenderId(2);
    o.setContent(content);
    o.setSentAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  private MessageDTO crearMessageDTO(Integer messageId, String content) {
    MessageDTO o = new MessageDTO();
    o.setMessageId(messageId);
    o.setConversationId(1);
    o.setSenderId(2);
    o.setContent(content);
    o.setSentAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  private InsertMessageDTO crearInsertMessageDTO(String content) {
    InsertMessageDTO o = new InsertMessageDTO();
    o.setConversationId(1);
    o.setSenderId(2);
    o.setContent(content);
    return o;
  }

  private UpdateMessageDTO crearUpdateMessageDTO(String content) {
    UpdateMessageDTO o = new UpdateMessageDTO();
    o.setContent(content);
    return o;
  }

  @Test
  @DisplayName("Test controlador getAll Ok")
  void getAllOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10);

    // ESPERADO
    List<MessageDTO> lista =
        List.of(crearMessageDTO(1, "Hola"), crearMessageDTO(2, "¿Sigue disponible?"));

    Pageable pageableEsperado = PageRequest.of(0, 10);

    Page<MessageDTO> pageEsperado = new PageImpl<>(lista, pageableEsperado, 2);

    ResponseEntity<Page<MessageDTO>> resEsperado = ResponseEntity.ok(pageEsperado);

    // MOCKS
    // CONVERTER MOCK (Pageable -> PageableModel)
    PageableModel pageableModelMock = new PageableModel(0, 10, "UNSORTED");

    when(messageInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);

    // USE CASE MOCK
    PageModel<Message> pageModelMock =
        new PageModel<>(
            List.of(crearMessage(1, "Hola"), crearMessage(2, "¿Sigue disponible?")),
            2,
            1,
            2,
            10,
            0);

    when(selectAllMessagesUseCase.selectAllMessages(pageableModelMock)).thenReturn(pageModelMock);

    // CONVERTER MOCK (PageModel -> Page de DTOs)
    Page<MessageDTO> pageDtoMock =
        new PageImpl<>(
            List.of(crearMessageDTO(1, "Hola"), crearMessageDTO(2, "¿Sigue disponible?")),
            PageRequest.of(0, 10),
            2);

    when(messageInConverter.toMessagesPage(pageModelMock, pageableModelMock))
        .thenReturn(pageDtoMock);

    // EJECUCIÓN
    ResponseEntity<Page<MessageDTO>> res = messageController.getAllMessages(pageableInput);

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

    when(messageInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);
    when(selectAllMessagesUseCase.selectAllMessages(pageableModelMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class, () -> messageController.getAllMessages(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(messageInConverter, never()).toMessagesPage(any(), any());
  }

  @Test
  @DisplayName("Test controlador insertIntoMessage Ok")
  void insertIntoMessageOkTest() {

    // INPUT
    InsertMessageDTO insertMessageDTOInput = crearInsertMessageDTO("Hola");

    // ESPERADO
    MessageDTO dtoEsperado = crearMessageDTO(3, "Hola");

    ResponseEntity<MessageDTO> resEsperado =
        ResponseEntity.status(HttpStatus.CREATED).body(dtoEsperado);

    // MOCKS
    Message messageNuevoMock = crearMessage(null, "Hola");
    Message messageCreadoMock = crearMessage(3, "Hola");

    when(messageInConverter.toDomainInsert(insertMessageDTOInput)).thenReturn(messageNuevoMock);
    when(newMessageUseCase.newMessage(messageNuevoMock)).thenReturn(messageCreadoMock);
    when(messageInConverter.toDto(messageCreadoMock)).thenReturn(crearMessageDTO(3, "Hola"));

    // EJECUCIÓN
    ResponseEntity<MessageDTO> res = messageController.insertIntoMessage(insertMessageDTOInput);

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
  @DisplayName("Test controlador insertIntoMessage Ko")
  void insertIntoMessageKoTest() {

    // INPUT
    InsertMessageDTO insertMessageDTOInput = crearInsertMessageDTO("Hola");

    // MOCKS
    Message messageNuevoMock = crearMessage(null, "Hola");

    when(messageInConverter.toDomainInsert(insertMessageDTOInput)).thenReturn(messageNuevoMock);
    when(newMessageUseCase.newMessage(messageNuevoMock))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> messageController.insertIntoMessage(insertMessageDTOInput));

    assertNotNull(ex.getMessage());
    verify(messageInConverter, never()).toDto(any(Message.class));
  }

  @Test
  @DisplayName("Test controlador getMessageById Ok")
  void getMessageByIdOkTest() {

    // INPUT
    Integer messageIdInput = 1;

    // ESPERADO
    MessageDTO dtoEsperado = crearMessageDTO(1, "Hola");

    ResponseEntity<MessageDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    Message messageMock = crearMessage(1, "Hola");

    when(selectMessageByIdUseCase.selectMessageById(messageIdInput)).thenReturn(messageMock);
    when(messageInConverter.toDto(messageMock)).thenReturn(crearMessageDTO(1, "Hola"));

    // EJECUCIÓN
    ResponseEntity<MessageDTO> res = messageController.getMessageById(messageIdInput);

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
  @DisplayName("Test controlador getMessageById Ko")
  void getMessageByIdKoTest() {

    // INPUT
    Integer messageIdInput = 99;

    // MOCKS
    when(selectMessageByIdUseCase.selectMessageById(messageIdInput))
        .thenThrow(new MessageNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    MessageNotFoundException ex =
        assertThrows(
            MessageNotFoundException.class, () -> messageController.getMessageById(messageIdInput));

    assertEquals("No se encuentra el mensaje con id: 99", ex.getMessage());
    verify(messageInConverter, never()).toDto(any(Message.class));
  }

  @Test
  @DisplayName("Test controlador updateMessage Ok")
  void updateMessageOkTest() {

    // INPUT
    Integer messageIdInput = 1;
    UpdateMessageDTO updateMessageDTOInput = crearUpdateMessageDTO("Mensaje editado");

    // ESPERADO
    MessageDTO dtoEsperado = crearMessageDTO(1, "Mensaje editado");

    ResponseEntity<MessageDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    Message messageNuevosDatosMock = crearMessage(null, "Mensaje editado");
    Message messageActualizadoMock = crearMessage(1, "Mensaje editado");

    when(messageInConverter.toDomainUpdate(updateMessageDTOInput))
        .thenReturn(messageNuevosDatosMock);
    when(updateMessageUseCase.updateMessage(messageIdInput, messageNuevosDatosMock))
        .thenReturn(messageActualizadoMock);
    when(messageInConverter.toDto(messageActualizadoMock))
        .thenReturn(crearMessageDTO(1, "Mensaje editado"));

    // EJECUCIÓN
    ResponseEntity<MessageDTO> res =
        messageController.updateMessage(messageIdInput, updateMessageDTOInput);

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
  @DisplayName("Test controlador updateMessage Ko")
  void updateMessageKoTest() {

    // INPUT
    Integer messageIdInput = 99;
    UpdateMessageDTO updateMessageDTOInput = crearUpdateMessageDTO("Mensaje editado");

    // MOCKS
    Message messageNuevosDatosMock = crearMessage(null, "Mensaje editado");

    when(messageInConverter.toDomainUpdate(updateMessageDTOInput))
        .thenReturn(messageNuevosDatosMock);
    when(updateMessageUseCase.updateMessage(messageIdInput, messageNuevosDatosMock))
        .thenThrow(new MessageNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    MessageNotFoundException ex =
        assertThrows(
            MessageNotFoundException.class,
            () -> messageController.updateMessage(messageIdInput, updateMessageDTOInput));

    assertEquals("No se encuentra el mensaje con id: 99", ex.getMessage());
    verify(messageInConverter, never()).toDto(any(Message.class));
  }

  @Test
  @DisplayName("Test controlador deleteMessage Ok")
  void deleteMessageOkTest() {

    // INPUT
    Integer messageIdInput = 1;

    // ESPERADO
    ResponseEntity<Void> resEsperado = ResponseEntity.noContent().build();

    // MOCKS
    doNothing().when(deleteMessageUseCase).deleteMessage(messageIdInput);

    // EJECUCIÓN
    ResponseEntity<Void> res = messageController.deleteMessage(messageIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
          assertNull(res.getBody());
          assertEquals(resEsperado, res);
        });
    verify(deleteMessageUseCase).deleteMessage(messageIdInput);
  }

  @Test
  @DisplayName("Test controlador deleteMessage Ko")
  void deleteMessageKoTest() {

    // INPUT
    Integer messageIdInput = 99;

    // MOCKS
    doThrow(new MessageNotFoundException(99))
        .when(deleteMessageUseCase)
        .deleteMessage(messageIdInput);

    // EJECUCIÓN Y VALIDACIONES
    MessageNotFoundException ex =
        assertThrows(
            MessageNotFoundException.class, () -> messageController.deleteMessage(messageIdInput));

    assertEquals("No se encuentra el mensaje con id: 99", ex.getMessage());
  }
}
