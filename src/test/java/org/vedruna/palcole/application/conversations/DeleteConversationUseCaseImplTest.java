package org.vedruna.palcole.application.conversations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.exception.conversations.ConversationNotFoundException;
import org.vedruna.palcole.domain.ports.outbound.ConversationStorage;

@ExtendWith(MockitoExtension.class)
class DeleteConversationUseCaseImplTest {

  @Mock private ConversationStorage conversationStorage;

  @InjectMocks private DeleteConversationUseCaseImpl deleteConversationUseCaseImpl;

  @Test
  @DisplayName("Test caso de uso deleteConversation Ok")
  void deleteConversationOkTest() {

    // INPUT
    Integer conversationIdInput = 1;

    // MOCKS
    doNothing().when(conversationStorage).deleteConversation(conversationIdInput);

    // EJECUCIÓN Y VALIDACIONES
    assertDoesNotThrow(() -> deleteConversationUseCaseImpl.deleteConversation(conversationIdInput));
    verify(conversationStorage).deleteConversation(conversationIdInput);
  }

  @Test
  @DisplayName("Test caso de uso deleteConversation Ko")
  void deleteConversationKoTest() {

    // INPUT
    Integer conversationIdInput = 99;

    // MOCKS
    doThrow(new ConversationNotFoundException(99))
        .when(conversationStorage)
        .deleteConversation(conversationIdInput);

    // EJECUCIÓN Y VALIDACIONES
    ConversationNotFoundException ex =
        assertThrows(
            ConversationNotFoundException.class,
            () -> deleteConversationUseCaseImpl.deleteConversation(conversationIdInput));

    assertEquals("No se encuentra la conversación con id: 99", ex.getMessage());
  }
}
