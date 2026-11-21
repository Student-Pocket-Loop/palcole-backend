package org.vedruna.palcole.application.conversations;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.exception.conversations.ConversationNotFoundException;
import org.vedruna.palcole.domain.model.Conversation;
import org.vedruna.palcole.domain.ports.outbound.ConversationStorage;

@ExtendWith(MockitoExtension.class)
class SelectConversationByIdUseCaseImplTest {

  @Mock private ConversationStorage conversationStorage;

  @InjectMocks private SelectConversationByIdUseCaseImpl selectConversationByIdUseCaseImpl;

  private Conversation crearConversation(Integer conversationId, Integer itemId) {
    Conversation o = new Conversation();
    o.setConversationId(conversationId);
    o.setItemId(itemId);
    o.setBuyerId(2);
    o.setStartedAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  @Test
  @DisplayName("Test caso de uso selectConversationById Ok")
  void selectConversationByIdOkTest() {

    // INPUT
    Integer conversationIdInput = 1;

    // ESPERADO
    Conversation conversationEsperado = crearConversation(1, 10);

    // MOCKS
    when(conversationStorage.findConversationById(conversationIdInput))
        .thenReturn(conversationEsperado);

    // EJECUCIÓN
    Conversation res =
        selectConversationByIdUseCaseImpl.selectConversationById(conversationIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(conversationEsperado, res);
        });
    verify(conversationStorage).findConversationById(conversationIdInput);
  }

  @Test
  @DisplayName("Test caso de uso selectConversationById Ko")
  void selectConversationByIdKoTest() {

    // INPUT
    Integer conversationIdInput = 99;

    // MOCKS
    when(conversationStorage.findConversationById(conversationIdInput))
        .thenThrow(new ConversationNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    ConversationNotFoundException ex =
        assertThrows(
            ConversationNotFoundException.class,
            () -> selectConversationByIdUseCaseImpl.selectConversationById(conversationIdInput));

    assertEquals("No se encuentra la conversación con id: 99", ex.getMessage());
  }
}
