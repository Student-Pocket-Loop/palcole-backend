package org.vedruna.palcole.infrastructure.adapters.inbound.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vedruna.palcole.domain.model.Message;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.messages.InsertMessageDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.messages.MessageDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.messages.UpdateMessageDTO;

class MessageInConverterTest {

  private final MessageInConverter messageInConverter = Mappers.getMapper(MessageInConverter.class);

  private Message crearMessage(Integer messageId, String content) {
    Message o = new Message();
    o.setMessageId(messageId);
    o.setConversationId(1);
    o.setSenderId(1);
    o.setContent(content);
    o.setSentAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  private MessageDTO crearMessageDTO(Integer messageId, String content) {
    MessageDTO o = new MessageDTO();
    o.setMessageId(messageId);
    o.setConversationId(1);
    o.setSenderId(1);
    o.setContent(content);
    o.setSentAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  private InsertMessageDTO crearInsertMessageDTO(String content) {
    InsertMessageDTO o = new InsertMessageDTO();
    o.setConversationId(1);
    o.setSenderId(1);
    o.setContent(content);
    return o;
  }

  private UpdateMessageDTO crearUpdateMessageDTO(String content) {
    UpdateMessageDTO o = new UpdateMessageDTO();
    o.setContent(content);
    return o;
  }

  @Test
  @DisplayName("Test converter toDto Ok")
  void toDtoOkTest() {

    // INPUT
    Message messageInput = crearMessage(1, "Hola, ¿sigue disponible?");

    // ESPERADO
    MessageDTO messageDtoEsperado = crearMessageDTO(1, "Hola, ¿sigue disponible?");

    // EJECUCIÓN
    MessageDTO res = messageInConverter.toDto(messageInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(messageDtoEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toDto Null")
  void toDtoNullTest() {

    // INPUT
    Message messageInput = null;

    // EJECUCIÓN
    MessageDTO res = messageInConverter.toDto(messageInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter toDomainInsert Ok")
  void toDomainInsertOkTest() {

    // INPUT
    InsertMessageDTO insertMessageDtoInput = crearInsertMessageDTO("Hola, ¿sigue disponible?");

    // ESPERADO
    Message messageEsperado = crearMessage(null, "Hola, ¿sigue disponible?");

    // EJECUCIÓN
    Message res = messageInConverter.toDomainInsert(insertMessageDtoInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(messageEsperado.getMessageId(), res.getMessageId());
          assertEquals(messageEsperado.getConversationId(), res.getConversationId());
          assertEquals(messageEsperado.getSenderId(), res.getSenderId());
          assertEquals(messageEsperado.getContent(), res.getContent());
          assertNotNull(res.getSentAt());
        });
  }

  @Test
  @DisplayName("Test converter toDomainInsert Null")
  void toDomainInsertNullTest() {

    // INPUT
    InsertMessageDTO insertMessageDtoInput = null;

    // EJECUCIÓN
    Message res = messageInConverter.toDomainInsert(insertMessageDtoInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter toDomainUpdate Ok")
  void toDomainUpdateOkTest() {

    // INPUT
    UpdateMessageDTO updateMessageDtoInput = crearUpdateMessageDTO("Mensaje editado");

    // ESPERADO
    Message messageEsperado = crearMessage(null, "Mensaje editado");
    messageEsperado.setConversationId(null);
    messageEsperado.setSenderId(null);

    // EJECUCIÓN
    Message res = messageInConverter.toDomainUpdate(updateMessageDtoInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(messageEsperado.getMessageId(), res.getMessageId());
          assertEquals(messageEsperado.getConversationId(), res.getConversationId());
          assertEquals(messageEsperado.getSenderId(), res.getSenderId());
          assertEquals(messageEsperado.getContent(), res.getContent());
          assertNotNull(res.getSentAt());
        });
  }

  @Test
  @DisplayName("Test converter toDomainUpdate Null")
  void toDomainUpdateNullTest() {

    // INPUT
    UpdateMessageDTO updateMessageDtoInput = null;

    // EJECUCIÓN
    Message res = messageInConverter.toDomainUpdate(updateMessageDtoInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter toPageableModel Unsorted Ok")
  void toPageableModelUnsortedOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10);

    // ESPERADO
    PageableModel pageableModelEsperado = new PageableModel(0, 10, "UNSORTED");

    // EJECUCIÓN
    PageableModel res = messageInConverter.toPageableModel(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageableModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toPageableModel Sorted Ok")
  void toPageableModelSortedOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10, Sort.by("messageId").ascending());

    // ESPERADO
    PageableModel pageableModelEsperado = new PageableModel(0, 10, "messageId: ASC");

    // EJECUCIÓN
    PageableModel res = messageInConverter.toPageableModel(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageableModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toPageableModel Ko")
  void toPageableModelKoTest() {

    // INPUT
    Pageable pageableInput = null;

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(
        NullPointerException.class, () -> messageInConverter.toPageableModel(pageableInput));
  }

  @Test
  @DisplayName("Test converter toMessagesPage Unsorted Ok")
  void toMessagesPageUnsortedOkTest() {

    // INPUT
    PageModel<Message> pageModelInput =
        new PageModel<>(
            List.of(
                crearMessage(1, "Hola, ¿sigue disponible?"),
                crearMessage(2, "Sí, todavía lo tengo")),
            2,
            1,
            2,
            10,
            0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    Page<MessageDTO> pageEsperado =
        new PageImpl<>(
            List.of(
                crearMessageDTO(1, "Hola, ¿sigue disponible?"),
                crearMessageDTO(2, "Sí, todavía lo tengo")),
            PageRequest.of(0, 10),
            2);

    // EJECUCIÓN
    Page<MessageDTO> res = messageInConverter.toMessagesPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toMessagesPage Sorted ASC Ok")
  void toMessagesPageSortedAscOkTest() {

    // INPUT
    PageModel<Message> pageModelInput =
        new PageModel<>(
            List.of(
                crearMessage(1, "Hola, ¿sigue disponible?"),
                crearMessage(2, "Sí, todavía lo tengo")),
            2,
            1,
            2,
            10,
            0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "messageId: ASC");

    // ESPERADO
    Page<MessageDTO> pageEsperado =
        new PageImpl<>(
            List.of(
                crearMessageDTO(1, "Hola, ¿sigue disponible?"),
                crearMessageDTO(2, "Sí, todavía lo tengo")),
            PageRequest.of(0, 10, Sort.by("messageId").ascending()),
            2);

    // EJECUCIÓN
    Page<MessageDTO> res = messageInConverter.toMessagesPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toMessagesPage Sorted DESC Ok")
  void toMessagesPageSortedDescOkTest() {

    // INPUT
    PageModel<Message> pageModelInput =
        new PageModel<>(
            List.of(
                crearMessage(1, "Hola, ¿sigue disponible?"),
                crearMessage(2, "Sí, todavía lo tengo")),
            2,
            1,
            2,
            10,
            0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "messageId: DESC");

    // ESPERADO
    Page<MessageDTO> pageEsperado =
        new PageImpl<>(
            List.of(
                crearMessageDTO(1, "Hola, ¿sigue disponible?"),
                crearMessageDTO(2, "Sí, todavía lo tengo")),
            PageRequest.of(0, 10, Sort.by("messageId").descending()),
            2);

    // EJECUCIÓN
    Page<MessageDTO> res = messageInConverter.toMessagesPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toMessagesPage Ko")
  void toMessagesPageKoTest() {

    // INPUT
    PageModel<Message> pageModelInput =
        new PageModel<>(List.of(crearMessage(1, "Hola, ¿sigue disponible?")), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "FORMATO_INVALIDO");

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(
        ArrayIndexOutOfBoundsException.class,
        () -> messageInConverter.toMessagesPage(pageModelInput, pageableModelInput));
  }
}
