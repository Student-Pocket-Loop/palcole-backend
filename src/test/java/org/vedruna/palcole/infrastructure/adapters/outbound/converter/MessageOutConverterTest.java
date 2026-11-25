package org.vedruna.palcole.infrastructure.adapters.outbound.converter;

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
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.ConversationEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.MessageEntity;
import org.vedruna.palcole.persistance.model.UserEntity;

class MessageOutConverterTest {

  private final MessageOutConverter messageOutConverter =
      Mappers.getMapper(MessageOutConverter.class);

  private Message crearMessage(Integer messageId, String content) {
    Message o = new Message();
    o.setMessageId(messageId);
    o.setConversationId(1);
    o.setSenderId(1);
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
    senderRef.setUserId(1);
    o.setSender(senderRef);
    o.setContent(content);
    o.setSentAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  @Test
  @DisplayName("Test converter toDomain Ok")
  void toDomainOkTest() {

    // INPUT
    MessageEntity messageEntityInput = crearMessageEntity(1, "Hola, ¿sigue disponible?");

    // ESPERADO
    Message messageEsperado = crearMessage(1, "Hola, ¿sigue disponible?");
    messageEsperado.setConversationId(null);
    messageEsperado.setSenderId(null);

    // EJECUCIÓN
    Message res = messageOutConverter.toDomain(messageEntityInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(messageEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toDomain Null")
  void toDomainNullTest() {

    // INPUT
    MessageEntity messageEntityInput = null;

    // EJECUCIÓN
    Message res = messageOutConverter.toDomain(messageEntityInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter toEntity Ok")
  void toEntityOkTest() {

    // INPUT
    Message messageInput = crearMessage(1, "Hola, ¿sigue disponible?");

    // ESPERADO
    MessageEntity messageEntityEsperado = crearMessageEntity(1, "Hola, ¿sigue disponible?");
    messageEntityEsperado.setConversation(null); // la relación la resuelve el storage
    messageEntityEsperado.setSender(null); // la relación la resuelve el storage

    // EJECUCIÓN
    MessageEntity res = messageOutConverter.toEntity(messageInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(messageEntityEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toEntity Null")
  void toEntityNullTest() {

    // INPUT
    Message messageInput = null;

    // EJECUCIÓN
    MessageEntity res = messageOutConverter.toEntity(messageInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter toPageable Unsorted Ok")
  void toPageableUnsortedOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10);

    // EJECUCIÓN
    Pageable res = messageOutConverter.toPageable(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageableEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toPageable Sorted ASC Ok")
  void toPageableSortedAscOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "messageId: ASC");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by("messageId").ascending());

    // EJECUCIÓN
    Pageable res = messageOutConverter.toPageable(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageableEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toPageable Sorted DESC Ok")
  void toPageableSortedDescOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "messageId: DESC");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by("messageId").descending());

    // EJECUCIÓN
    Pageable res = messageOutConverter.toPageable(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageableEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toPageable Ko")
  void toPageableKoTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "FORMATO_INVALIDO");

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(
        ArrayIndexOutOfBoundsException.class,
        () -> messageOutConverter.toPageable(pageableModelInput));
  }

  @Test
  @DisplayName("Test converter toMessagePageModel Ok")
  void toMessagePageModelOkTest() {

    // INPUT
    Pageable pageable = PageRequest.of(0, 10);
    Page<MessageEntity> pageInput =
        new PageImpl<>(
            List.of(
                crearMessageEntity(1, "Hola, ¿sigue disponible?"),
                crearMessageEntity(2, "Sí, todavía lo tengo")),
            pageable,
            2);

    // ESPERADO
    Message m1 = crearMessage(1, "Hola, ¿sigue disponible?");
    m1.setConversationId(null);
    m1.setSenderId(null);

    Message m2 = crearMessage(2, "Sí, todavía lo tengo");
    m2.setConversationId(null);
    m2.setSenderId(null);

    PageModel<Message> pageModelEsperado = new PageModel<>(List.of(m1, m2), 2, 1, 2, 10, 0);

    // EJECUCIÓN
    PageModel<Message> res = messageOutConverter.toMessagePageModel(pageInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toMessagePageModel Ko")
  void toMessagePageModelKoTest() {

    // INPUT
    Page<MessageEntity> pageInput = null;

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(
        NullPointerException.class, () -> messageOutConverter.toMessagePageModel(pageInput));
  }
}
