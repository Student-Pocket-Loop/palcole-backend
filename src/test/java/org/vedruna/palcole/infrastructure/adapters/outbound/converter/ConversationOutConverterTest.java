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
import org.vedruna.palcole.domain.model.Conversation;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.AcademicItemEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.ConversationEntity;
import org.vedruna.palcole.persistance.model.UserEntity;

class ConversationOutConverterTest {

  private final ConversationOutConverter conversationOutConverter =
      Mappers.getMapper(ConversationOutConverter.class);

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
  @DisplayName("Test converter toDomain Ok")
  void toDomainOkTest() {

    // INPUT
    ConversationEntity conversationEntityInput = crearConversationEntity(1, 10);

    // ESPERADO
    Conversation conversationEsperado = crearConversation(1, 10);

    // EJECUCIÓN
    Conversation res = conversationOutConverter.toDomain(conversationEntityInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(conversationEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toDomain Null")
  void toDomainNullTest() {

    // INPUT
    ConversationEntity conversationEntityInput = null;

    // EJECUCIÓN
    Conversation res = conversationOutConverter.toDomain(conversationEntityInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter toEntity Ok")
  void toEntityOkTest() {

    // INPUT
    Conversation conversationInput = crearConversation(1, 10);

    // ESPERADO
    ConversationEntity conversationEntityEsperado = crearConversationEntity(1, 10);
    conversationEntityEsperado.setItem(null); // la relación la resuelve el storage
    conversationEntityEsperado.setBuyer(null); // la relación la resuelve el storage

    // EJECUCIÓN
    ConversationEntity res = conversationOutConverter.toEntity(conversationInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(conversationEntityEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toEntity Null")
  void toEntityNullTest() {

    // INPUT
    Conversation conversationInput = null;

    // EJECUCIÓN
    ConversationEntity res = conversationOutConverter.toEntity(conversationInput);

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
    Pageable res = conversationOutConverter.toPageable(pageableModelInput);

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
    PageableModel pageableModelInput = new PageableModel(0, 10, "conversationId: ASC");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by("conversationId").ascending());

    // EJECUCIÓN
    Pageable res = conversationOutConverter.toPageable(pageableModelInput);

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
    PageableModel pageableModelInput = new PageableModel(0, 10, "conversationId: DESC");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by("conversationId").descending());

    // EJECUCIÓN
    Pageable res = conversationOutConverter.toPageable(pageableModelInput);

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
        () -> conversationOutConverter.toPageable(pageableModelInput));
  }

  @Test
  @DisplayName("Test converter toConversationPageModel Ok")
  void toConversationPageModelOkTest() {

    // INPUT
    Pageable pageable = PageRequest.of(0, 10);
    Page<ConversationEntity> pageInput =
        new PageImpl<>(
            List.of(crearConversationEntity(1, 10), crearConversationEntity(2, 20)), pageable, 2);

    // ESPERADO
    PageModel<Conversation> pageModelEsperado =
        new PageModel<>(
            List.of(crearConversation(1, 10), crearConversation(2, 20)), 2, 1, 2, 10, 0);

    // EJECUCIÓN
    PageModel<Conversation> res = conversationOutConverter.toConversationPageModel(pageInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toConversationPageModel Ko")
  void toConversationPageModelKoTest() {

    // INPUT
    Page<ConversationEntity> pageInput = null;

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(
        NullPointerException.class,
        () -> conversationOutConverter.toConversationPageModel(pageInput));
  }
}
