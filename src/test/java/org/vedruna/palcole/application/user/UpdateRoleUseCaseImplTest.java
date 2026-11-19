package org.vedruna.palcole.application.user;

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
import org.vedruna.palcole.application.User.UpdateUserUseCaseImpl;
import org.vedruna.palcole.domain.exception.users.UserNotFoundException;
import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseImplTest {

  @Mock private UserStorage userStorage;

  @InjectMocks private UpdateUserUseCaseImpl updateUserUseCaseImpl;

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
  @DisplayName("Test caso de uso updateUser Ok")
  void updateUserOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    Integer userIdInput = 1;
    User newUserInput = crearUser(null, "maria.garcia", fechaFija);

    // ESPERADO
    User userEsperado = crearUser(1, "maria.garcia", fechaFija);

    // MOCKS
    User userActualizadoMock = crearUser(1, "maria.garcia", fechaFija);

    when(userStorage.updateUser(userIdInput, newUserInput)).thenReturn(userActualizadoMock);

    // EJECUCIÓN
    User res = updateUserUseCaseImpl.updateUser(userIdInput, newUserInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(userEsperado, res);
        });
    verify(userStorage).updateUser(userIdInput, newUserInput);
  }

  @Test
  @DisplayName("Test caso de uso updateUser Ko")
  void updateUserKoTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    Integer userIdInput = 99;
    User newUserInput = crearUser(null, "maria.garcia", fechaFija);

    // MOCKS
    when(userStorage.updateUser(userIdInput, newUserInput))
        .thenThrow(new UserNotFoundException(userIdInput));

    // EJECUCIÓN Y VALIDACIONES
    UserNotFoundException ex =
        assertThrows(
            UserNotFoundException.class,
            () -> updateUserUseCaseImpl.updateUser(userIdInput, newUserInput));

    assertEquals("No se encuentra el usuario con id: 99", ex.getMessage());
    verify(userStorage).updateUser(userIdInput, newUserInput);
  }
}
