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
import org.vedruna.palcole.domain.exception.messages.MessageNotFoundException;
import org.vedruna.palcole.domain.model.Message;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.MessageOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.ConversationEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.MessageEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.MessageRepository;
import org.vedruna.palcole.persistance.model.UserEntity;

@ExtendWith(MockitoExtension.class)
class MessageStorageImplTest {

  @Mock private MessageRepository messageRepository;

  @Mock private MessageOutConverter messageOutConverter;

  @InjectMocks private MessageStorageImpl messageStorageImpl;

  private Message crearMessage(Integer messageId, String content) {
    Message o = new Message();
    o.setMessageId(messageId);
    o.setConversationId(1);
    o.setSenderId(2);
    o.setContent(content);
    o.setSentAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  private MessageEntity crearMessageEntity(Integer messageId, String content) {
    MessageEntity o = new MessageEntity();
    o.setMessageId(messageId);
    ConversationEntity conversationRef = new ConversationEntity();
    conversationRef.setConversationId(1);
    o.setConversation(conversationRef);
    UserEntity senderRef = new UserEntity();
    senderRef.setUserId(2);
    o.setSender(senderRef);
    o.setContent(content);
    o.setSentAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  @Test
  @DisplayName("Test storage selectAllMessages Ok")
  void selectAllMessagesOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<Message> pageModelEsperado =
        new PageModel<>(
            List.of(crearMessage(1, "Hola"), crearMessage(2, "¿Sigue disponible?")),
            2,
            1,
            2,
            10,
            0);

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);
    Page<MessageEntity> pageEntityMock =
        new PageImpl<>(
            List.of(crearMessageEntity(1, "Hola"), crearMessageEntity(2, "¿Sigue disponible?")),
            pageableMock,
            2);
    PageModel<Message> pageModelMock =
        new PageModel<>(
            List.of(crearMessage(1, "Hola"), crearMessage(2, "¿Sigue disponible?")),
            2,
            1,
            2,
            10,
            0);

    when(messageOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(messageRepository.findAll(pageableMock)).thenReturn(pageEntityMock);
    when(messageOutConverter.toMessagePageModel(pageEntityMock)).thenReturn(pageModelMock);

    // EJECUCIÓN
    PageModel<Message> res = messageStorageImpl.selectAllMessages(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage selectAllMessages Ko")
  void selectAllMessagesKoTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "INVALID_SORT");

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);

    when(messageOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(messageRepository.findAll(pageableMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> messageStorageImpl.selectAllMessages(pageableModelInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(messageOutConverter, never()).toMessagePageModel(any());
  }

  @Test
  @DisplayName("Test storage findMessageById Ok")
  void findMessageByIdOkTest() {

    // INPUT
    Integer messageIdInput = 1;

    // ESPERADO
    Message messageEsperado = crearMessage(1, "Hola");

    // MOCKS
    MessageEntity messageEntityMock = crearMessageEntity(1, "Hola");
    Message messageMock = crearMessage(1, "Hola");

    when(messageRepository.findById(messageIdInput)).thenReturn(Optional.of(messageEntityMock));
    when(messageOutConverter.toDomain(messageEntityMock)).thenReturn(messageMock);

    // EJECUCIÓN
    Message res = messageStorageImpl.findMessageById(messageIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(messageEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage findMessageById Ko")
  void findMessageByIdKoTest() {

    // INPUT
    Integer messageIdInput = 99;

    // MOCKS
    when(messageRepository.findById(messageIdInput)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    MessageNotFoundException ex =
        assertThrows(
            MessageNotFoundException.class,
            () -> messageStorageImpl.findMessageById(messageIdInput));

    assertEquals("No se encuentra el mensaje con id: 99", ex.getMessage());
    verify(messageOutConverter, never()).toDomain(any(MessageEntity.class));
  }

  @Test
  @DisplayName("Test storage updateMessage Ok")
  void updateMessageOkTest() {

    // INPUT
    Integer messageIdInput = 1;
    Message newMessageInput = crearMessage(null, "Mensaje editado");

    // ESPERADO
    Message messageEsperado = crearMessage(1, "Mensaje editado");

    // MOCKS
    MessageEntity messageEntityExistenteMock = crearMessageEntity(1, "Hola");
    MessageEntity messageEntityGuardadoMock = crearMessageEntity(1, "Mensaje editado");
    Message messageActualizadoMock = crearMessage(1, "Mensaje editado");

    when(messageRepository.findById(messageIdInput))
        .thenReturn(Optional.of(messageEntityExistenteMock));
    when(messageRepository.save(messageEntityExistenteMock)).thenReturn(messageEntityGuardadoMock);
    when(messageOutConverter.toDomain(messageEntityGuardadoMock))
        .thenReturn(messageActualizadoMock);

    // EJECUCIÓN
    Message res = messageStorageImpl.updateMessage(messageIdInput, newMessageInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals("Mensaje editado", messageEntityExistenteMock.getContent());
          assertEquals(messageEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage updateMessage Ko")
  void updateMessageKoTest() {

    // INPUT
    Integer messageIdInput = 99;
    Message newMessageInput = crearMessage(null, "Mensaje editado");

    // MOCKS
    when(messageRepository.findById(messageIdInput)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    MessageNotFoundException ex =
        assertThrows(
            MessageNotFoundException.class,
            () -> messageStorageImpl.updateMessage(messageIdInput, newMessageInput));

    assertEquals("No se encuentra el mensaje con id: 99", ex.getMessage());
    verify(messageRepository, never()).save(any(MessageEntity.class));
    verify(messageOutConverter, never()).toDomain(any(MessageEntity.class));
  }

  @Test
  @DisplayName("Test storage insertMessage Ok")
  void insertMessageOkTest() {

    // INPUT
    Message newMessageInput = crearMessage(null, "Hola");

    // ESPERADO
    Message messageEsperado = crearMessage(3, "Hola");

    // MOCKS
    MessageEntity messageEntityNuevoMock = crearMessageEntity(null, "Hola");
    MessageEntity messageEntityGuardadoMock = crearMessageEntity(3, "Hola");
    Message messageCreadoMock = crearMessage(3, "Hola");

    when(messageOutConverter.toEntity(newMessageInput)).thenReturn(messageEntityNuevoMock);
    when(messageRepository.save(messageEntityNuevoMock)).thenReturn(messageEntityGuardadoMock);
    when(messageOutConverter.toDomain(messageEntityGuardadoMock)).thenReturn(messageCreadoMock);

    // EJECUCIÓN
    Message res = messageStorageImpl.insertMessage(newMessageInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(messageEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage insertMessage Ko")
  void insertMessageKoTest() {

    // INPUT
    Message newMessageInput = crearMessage(null, "Hola");

    // MOCKS
    MessageEntity messageEntityNuevoMock = crearMessageEntity(null, "Hola");

    when(messageOutConverter.toEntity(newMessageInput)).thenReturn(messageEntityNuevoMock);
    when(messageRepository.save(messageEntityNuevoMock))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> messageStorageImpl.insertMessage(newMessageInput));

    assertEquals("Conflicto con los datos existentes", ex.getMessage());
    verify(messageOutConverter, never()).toDomain(any(MessageEntity.class));
  }

  @Test
  @DisplayName("Test storage deleteMessage Ok")
  void deleteMessageOkTest() {

    // INPUT
    Integer messageIdInput = 1;

    // MOCKS
    when(messageRepository.existsById(messageIdInput)).thenReturn(true);
    doNothing().when(messageRepository).deleteById(messageIdInput);

    // EJECUCIÓN
    messageStorageImpl.deleteMessage(messageIdInput);

    // VALIDACIONES
    verify(messageRepository).existsById(messageIdInput);
    verify(messageRepository).deleteById(messageIdInput);
  }

  @Test
  @DisplayName("Test storage deleteMessage Ko")
  void deleteMessageKoTest() {

    // INPUT
    Integer messageIdInput = 99;

    // MOCKS
    when(messageRepository.existsById(messageIdInput)).thenReturn(false);

    // EJECUCIÓN Y VALIDACIONES
    MessageNotFoundException ex =
        assertThrows(
            MessageNotFoundException.class, () -> messageStorageImpl.deleteMessage(messageIdInput));

    assertEquals("No se encuentra el mensaje con id: 99", ex.getMessage());
    verify(messageRepository, never()).deleteById(any());
  }
}
