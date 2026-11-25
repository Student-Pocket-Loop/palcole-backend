package org.vedruna.palcole.application.messages;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.model.Message;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.MessageStorage;

@ExtendWith(MockitoExtension.class)
class SelectAllMessagesUseCaseImplTest {

  @Mock private MessageStorage messageStorage;

  @InjectMocks private SelectAllMessagesUseCaseImpl selectAllMessagesUseCaseImpl;

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
  @DisplayName("Test caso de uso selectAllMessages Ok")
  void selectAllMessagesOkTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<Message> pageEsperado =
        new PageModel<>(
            List.of(crearMessage(1, "Hola"), crearMessage(2, "¿Sigue disponible?")),
            2,
            1,
            2,
            10,
            0);

    // MOCKS
    when(messageStorage.selectAllMessages(pageableInput)).thenReturn(pageEsperado);

    // EJECUCIÓN
    PageModel<Message> res = selectAllMessagesUseCaseImpl.selectAllMessages(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(2, res.getTotalElements());
          assertEquals(pageEsperado, res);
        });
    verify(messageStorage).selectAllMessages(pageableInput);
  }

  @Test
  @DisplayName("Test caso de uso selectAllMessages Ko")
  void selectAllMessagesKoTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "inexistente: ASC");

    // MOCKS
    when(messageStorage.selectAllMessages(pageableInput))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> selectAllMessagesUseCaseImpl.selectAllMessages(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
  }
}
