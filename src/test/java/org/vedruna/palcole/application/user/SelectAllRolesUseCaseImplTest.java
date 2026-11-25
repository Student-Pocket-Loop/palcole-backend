package org.vedruna.palcole.application.user;

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
import org.vedruna.palcole.application.User.SelectAllUsersUseCaseImpl;
import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;

@ExtendWith(MockitoExtension.class)
class SelectAllUsersUseCaseImplTest {

  @Mock private UserStorage userStorage;

  @InjectMocks private SelectAllUsersUseCaseImpl selectAllUsersUseCaseImpl;

  private User crearUser(Integer userId, String username, LocalDateTime fechaFija) {
    User user = new User();
    user.setUserId(userId);
    user.setRolesRoleId(1);
    user.setUsername(username);
    user.setEmail(username.toLowerCase() + "@vedruna.org");
    user.setPassword("password123");
    user.setCreateDate(fechaFija);
    return user;
  }

  @Test
  @DisplayName("Test caso de uso selectAllUsers Ok")
  void selectAllUsersOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<User> pageEsperado =
        new PageModel<>(
            List.of(crearUser(1, "admin", fechaFija), crearUser(2, "juan.perez", fechaFija)),
            2,
            1,
            2,
            10,
            0);

    // MOCKS
    when(userStorage.selectAllUsers(pageableInput)).thenReturn(pageEsperado);

    // EJECUCIÓN
    PageModel<User> res = selectAllUsersUseCaseImpl.selectAllUsers(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals("admin", res.getContent().get(0).getUsername());
          assertEquals("juan.perez", res.getContent().get(1).getUsername());
          assertEquals(2, res.getTotalElements());
        });
    verify(userStorage).selectAllUsers(pageableInput);
  }

  @Test
  @DisplayName("Test caso de uso selectAllUsers Ko")
  void selectAllUsersKoTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "inexistente: ASC");

    // MOCKS
    when(userStorage.selectAllUsers(pageableInput))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> selectAllUsersUseCaseImpl.selectAllUsers(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
  }
}
