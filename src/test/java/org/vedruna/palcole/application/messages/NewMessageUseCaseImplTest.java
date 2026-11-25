package org.vedruna.palcole.application.messages;

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
import org.vedruna.palcole.domain.model.Message;
import org.vedruna.palcole.domain.ports.outbound.MessageStorage;

@ExtendWith(MockitoExtension.class)
class NewMessageUseCaseImplTest {

  @Mock private MessageStorage messageStorage;

  @InjectMocks private NewMessageUseCaseImpl newMessageUseCaseImpl;

  private Message crearMessage(Integer messageId, String content) {
    Message o = new Message();
    o.setMessageId(messageId);
    o.setConversationId(1);
    o.setSenderId(2);
    o.setContent(content);
    o.setSentAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  @Test
  @DisplayName("Test caso de uso newMessage Ok")
  void newMessageOkTest() {

    // INPUT
    Message newMessageInput = crearMessage(null, "Hola");

    // ESPERADO
    Message messageEsperado = crearMessage(3, "Hola");

    // MOCKS
    Message messageCreadoMock = crearMessage(3, "Hola");

    when(messageStorage.insertMessage(newMessageInput)).thenReturn(messageCreadoMock);

    // EJECUCIÓN
    Message res = newMessageUseCaseImpl.newMessage(newMessageInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(messageEsperado, res);
          assertNotNull(newMessageInput.getSentAt());
        });
    verify(messageStorage).insertMessage(newMessageInput);
  }

  @Test
  @DisplayName("Test caso de uso newMessage Ko")
  void newMessageKoTest() {

    // INPUT
    Message newMessageInput = crearMessage(null, "Hola");

    // MOCKS
    when(messageStorage.insertMessage(newMessageInput))
        .thenThrow(new DataIntegrityViolationException("Conflicto con los datos existentes"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class,
            () -> newMessageUseCaseImpl.newMessage(newMessageInput));

    assertNotNull(ex.getMessage());
    verify(messageStorage).insertMessage(newMessageInput);
  }
}
