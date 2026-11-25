package org.vedruna.palcole.application.conversations;

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
import org.vedruna.palcole.domain.model.Conversation;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.ConversationStorage;

@ExtendWith(MockitoExtension.class)
class SelectAllConversationsUseCaseImplTest {

  @Mock private ConversationStorage conversationStorage;

  @InjectMocks private SelectAllConversationsUseCaseImpl selectAllConversationsUseCaseImpl;

  private Conversation crearConversation(Integer conversationId, Integer itemId) {
    Conversation o = new Conversation();
    o.setConversationId(conversationId);
    o.setItemId(itemId);
    o.setBuyerId(2);
    o.setStartedAt(LocalDateTime.of(2026, 9, 1, 10, 0));
    return o;
  }

  @Test
  @DisplayName("Test caso de uso selectAllConversations Ok")
  void selectAllConversationsOkTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<Conversation> pageEsperado =
        new PageModel<>(
            List.of(crearConversation(1, 10), crearConversation(2, 20)), 2, 1, 2, 10, 0);

    // MOCKS
    when(conversationStorage.selectAllConversations(pageableInput)).thenReturn(pageEsperado);

    // EJECUCIÓN
    PageModel<Conversation> res =
        selectAllConversationsUseCaseImpl.selectAllConversations(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(2, res.getTotalElements());
          assertEquals(pageEsperado, res);
        });
    verify(conversationStorage).selectAllConversations(pageableInput);
  }

  @Test
  @DisplayName("Test caso de uso selectAllConversations Ko")
  void selectAllConversationsKoTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "inexistente: ASC");

    // MOCKS
    when(conversationStorage.selectAllConversations(pageableInput))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> selectAllConversationsUseCaseImpl.selectAllConversations(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
  }
}
