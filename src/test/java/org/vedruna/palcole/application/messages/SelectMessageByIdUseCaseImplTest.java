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
import org.vedruna.palcole.domain.exception.MessageNotFoundException;
import org.vedruna.palcole.domain.model.Message;
import org.vedruna.palcole.domain.ports.outbound.MessageStorage;

@ExtendWith(MockitoExtension.class)
class SelectMessageByIdUseCaseImplTest {

  @Mock private MessageStorage messageStorage;

  @InjectMocks private SelectMessageByIdUseCaseImpl selectMessageByIdUseCaseImpl;

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
  @DisplayName("Test caso de uso selectMessageById Ok")
  void selectMessageByIdOkTest() {

    // INPUT
    Integer messageIdInput = 1;

    // ESPERADO
    Message messageEsperado = crearMessage(1, "Hola");

    // MOCKS
    when(messageStorage.findMessageById(messageIdInput)).thenReturn(messageEsperado);

    // EJECUCIÓN
    Message res = selectMessageByIdUseCaseImpl.selectMessageById(messageIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(messageEsperado, res);
        });
    verify(messageStorage).findMessageById(messageIdInput);
  }

  @Test
  @DisplayName("Test caso de uso selectMessageById Ko")
  void selectMessageByIdKoTest() {

    // INPUT
    Integer messageIdInput = 99;

    // MOCKS
    when(messageStorage.findMessageById(messageIdInput))
        .thenThrow(new MessageNotFoundException(99));

    // EJECUCIÓN Y VALIDACIONES
    MessageNotFoundException ex =
        assertThrows(
            MessageNotFoundException.class,
            () -> selectMessageByIdUseCaseImpl.selectMessageById(messageIdInput));

    assertEquals("No se encuentra el mensaje con id: 99", ex.getMessage());
  }
}
