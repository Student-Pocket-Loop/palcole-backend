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
import org.vedruna.palcole.application.User.NewUserUseCaseImpl;
import org.vedruna.palcole.domain.exception.user.UserAlreadyExistsException;
import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;

@ExtendWith(MockitoExtension.class)
class NewUserUseCaseImplTest {

  @Mock private UserStorage userStorage;

  @InjectMocks private NewUserUseCaseImpl newUserUseCaseImpl;

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
  @DisplayName("Test caso de uso newUser Ok")
  void newUserOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    User newUserInput = crearUser(null, "juan.perez", fechaFija);

    // ESPERADO
    User userEsperado = crearUser(3, "juan.perez", fechaFija);

    // MOCKS
    User userCreadoMock = crearUser(3, "juan.perez", fechaFija);

    when(userStorage.insertUser(newUserInput)).thenReturn(userCreadoMock);

    // EJECUCIÓN
    User res = newUserUseCaseImpl.newUser(newUserInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(userEsperado, res);
        });
    verify(userStorage).insertUser(newUserInput);
  }

  @Test
  @DisplayName("Test caso de uso newUser Ko")
  void newUserKoTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    User newUserInput = crearUser(null, "admin", fechaFija);

    // MOCKS
    when(userStorage.insertUser(newUserInput)).thenThrow(new UserAlreadyExistsException("admin"));

    // EJECUCIÓN Y VALIDACIONES
    UserAlreadyExistsException ex =
        assertThrows(
            UserAlreadyExistsException.class, () -> newUserUseCaseImpl.newUser(newUserInput));

    assertEquals("Ya existe un usuario con el nombre: admin", ex.getMessage());
    verify(userStorage).insertUser(newUserInput);
  }
}
