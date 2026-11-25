package org.vedruna.palcole.application.messages;

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
import org.vedruna.palcole.domain.exception.MessageNotFoundException;
import org.vedruna.palcole.domain.ports.outbound.MessageStorage;

@ExtendWith(MockitoExtension.class)
class DeleteMessageUseCaseImplTest {

  @Mock private MessageStorage messageStorage;

  @InjectMocks private DeleteMessageUseCaseImpl deleteMessageUseCaseImpl;

  @Test
  @DisplayName("Test caso de uso deleteMessage Ok")
  void deleteMessageOkTest() {

    // INPUT
    Integer messageIdInput = 1;

    // MOCKS
    doNothing().when(messageStorage).deleteMessage(messageIdInput);

    // EJECUCIÓN Y VALIDACIONES
    assertDoesNotThrow(() -> deleteMessageUseCaseImpl.deleteMessage(messageIdInput));
    verify(messageStorage).deleteMessage(messageIdInput);
  }

  @Test
  @DisplayName("Test caso de uso deleteMessage Ko")
  void deleteMessageKoTest() {

    // INPUT
    Integer messageIdInput = 99;

    // MOCKS
    doThrow(new MessageNotFoundException(99)).when(messageStorage).deleteMessage(messageIdInput);

    // EJECUCIÓN Y VALIDACIONES
    MessageNotFoundException ex =
        assertThrows(
            MessageNotFoundException.class,
            () -> deleteMessageUseCaseImpl.deleteMessage(messageIdInput));

    assertEquals("No se encuentra el mensaje con id: 99", ex.getMessage());
  }
}
