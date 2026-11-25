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
import org.springframework.dao.DataIntegrityViolationException;
import org.vedruna.palcole.domain.model.Conversation;
import org.vedruna.palcole.domain.ports.outbound.ConversationStorage;

@ExtendWith(MockitoExtension.class)
class NewConversationUseCaseImplTest {

  @Mock private ConversationStorage conversationStorage;

  @InjectMocks private NewConversationUseCaseImpl newConversationUseCaseImpl;

  private Conversation crearConversation(Integer conversationId, Integer itemId) {
    Conversation o = new Conversation();
    o.setConversationId(conversationId);
    o.setItemId(itemId);
    o.setBuyerId(2);
    o.setStartedAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  @Test
  @DisplayName("Test caso de uso newConversation Ok")
  void newConversationOkTest() {

    // INPUT
    Conversation newConversationInput = crearConversation(null, 10);

    // ESPERADO
    Conversation conversationEsperado = crearConversation(3, 10);

    // MOCKS
    Conversation conversationCreadoMock = crearConversation(3, 10);

    when(conversationStorage.insertConversation(newConversationInput))
        .thenReturn(conversationCreadoMock);

    // EJECUCIÓN
    Conversation res = newConversationUseCaseImpl.newConversation(newConversationInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(conversationEsperado, res);
          assertNotNull(res.getStartedAt());
        });
    verify(conversationStorage).insertConversation(newConversationInput);
  }

  @Test
  @DisplayName("Test caso de uso newConversation Ko")
  void newConversationKoTest() {

    // INPUT
    Conversation newConversationInput = crearConversation(null, 10);

    // MOCKS
    when(conversationStorage.insertConversation(newConversationInput))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> newConversationUseCaseImpl.newConversation(newConversationInput));

    assertNotNull(ex.getMessage());
    verify(conversationStorage).insertConversation(newConversationInput);
  }
}
