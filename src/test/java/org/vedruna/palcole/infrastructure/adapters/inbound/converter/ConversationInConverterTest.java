package org.vedruna.palcole.infrastructure.adapters.inbound.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.conversations.ConversationDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.conversations.InsertConversationDTO;

class ConversationInConverterTest {

  private final ConversationInConverter converter =
      Mappers.getMapper(ConversationInConverter.class);

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
  @DisplayName("Test converter inbound toDto Ok")
  void toDtoOkTest() {

    // INPUT
    Conversation conversationInput = crearConversation(1, 10);

    // ESPERADO
    ConversationDTO dtoEsperado = crearConversationDTO(1, 10);

    // EJECUCIÓN
    ConversationDTO res = converter.toDto(conversationInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(dtoEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter inbound toDto Null")
  void toDtoNullTest() {

    // INPUT
    Conversation conversationInput = null;

    // EJECUCIÓN
    ConversationDTO res = converter.toDto(conversationInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter inbound toDomainInsert Ok")
  void toDomainInsertOkTest() {

    // INPUT
    InsertConversationDTO dtoInput = crearInsertConversationDTO(10);

    // EJECUCIÓN
    Conversation res = converter.toDomainInsert(dtoInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertNull(res.getConversationId());
          assertEquals(dtoInput.getItemId(), res.getItemId());
          assertEquals(dtoInput.getBuyerId(), res.getBuyerId());
          assertNotNull(res.getStartedAt());
        });
  }

  @Test
  @DisplayName("Test converter inbound toDomainInsert Null")
  void toDomainInsertNullTest() {

    // INPUT
    InsertConversationDTO dtoInput = null;

    // EJECUCIÓN
    Conversation res = converter.toDomainInsert(dtoInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter inbound toPageableModel Ok (sin ordenar)")
  void toPageableModelUnsortedOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(1, 5);

    // EJECUCIÓN
    PageableModel res = converter.toPageableModel(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertEquals(1, res.getPage());
          assertEquals(5, res.getSize());
          assertEquals("UNSORTED", res.getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toPageableModel Ok (ordenado)")
  void toPageableModelSortedOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10, Sort.by("conversationId").ascending());

    // EJECUCIÓN
    PageableModel res = converter.toPageableModel(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertEquals(0, res.getPage());
          assertEquals(10, res.getSize());
          assertEquals("conversationId: ASC", res.getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toConversationsPage Ok (sin ordenar)")
  void toConversationsPageUnsortedOkTest() {

    // INPUT
    PageModel<Conversation> pageModelInput =
        new PageModel<>(
            List.of(crearConversation(1, 10), crearConversation(2, 20)), 2, 1, 2, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    Page<ConversationDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearConversationDTO(1, 10), crearConversationDTO(2, 20)),
            PageRequest.of(0, 10),
            2);

    // EJECUCIÓN
    Page<ConversationDTO> res = converter.toConversationsPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(2, res.getTotalElements());
          assertEquals(pageEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter inbound toConversationsPage Ok (orden ascendente)")
  void toConversationsPageAscOkTest() {

    // INPUT
    PageModel<Conversation> pageModelInput =
        new PageModel<>(List.of(crearConversation(1, 10)), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "conversationId: ASC");

    // ESPERADO
    Page<ConversationDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearConversationDTO(1, 10)),
            PageRequest.of(0, 10, Sort.by("conversationId").ascending()),
            1);

    // EJECUCIÓN
    Page<ConversationDTO> res = converter.toConversationsPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("conversationId").ascending(), res.getPageable().getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toConversationsPage Ok (orden descendente)")
  void toConversationsPageDescOkTest() {

    // INPUT
    PageModel<Conversation> pageModelInput =
        new PageModel<>(List.of(crearConversation(1, 10)), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "conversationId: DESC");

    // ESPERADO
    Page<ConversationDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearConversationDTO(1, 10)),
            PageRequest.of(0, 10, Sort.by("conversationId").descending()),
            1);

    // EJECUCIÓN
    Page<ConversationDTO> res = converter.toConversationsPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("conversationId").descending(), res.getPageable().getSort());
        });
  }
}
