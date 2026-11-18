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
import org.vedruna.palcole.application.User.SelectUserByIdUseCaseImpl;
import org.vedruna.palcole.domain.exception.user.UserNotFoundException;
import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;

@ExtendWith(MockitoExtension.class)
class SelectUserByIdUseCaseImplTest {

  @Mock private UserStorage userStorage;

  @InjectMocks private SelectUserByIdUseCaseImpl selectUserByIdUseCaseImpl;

  @Test
  @DisplayName("Test caso de uso selectUserById Ok")
  void selectUserByIdOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    Integer userIdInput = 1;

    // ESPERADO
    User userEsperado = new User();
    userEsperado.setUserId(1);
    userEsperado.setRolesRoleId(1);
    userEsperado.setUsername("admin");
    userEsperado.setEmail("admin@vedruna.org");
    userEsperado.setPassword("password123");
    userEsperado.setCreateDate(fechaFija);

    // MOCKS
    when(userStorage.findUserById(userIdInput)).thenReturn(userEsperado);

    // EJECUCIÓN
    User res = selectUserByIdUseCaseImpl.selectUserById(userIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(1, res.getUserId());
          assertEquals("admin", res.getUsername());
          assertEquals("admin@vedruna.org", res.getEmail());
        });
    verify(userStorage).findUserById(userIdInput);
  }

  @Test
  @DisplayName("Test caso de uso selectUserById Ko")
  void selectUserByIdKoTest() {

    // INPUT
    Integer userIdInput = 99;

    // MOCKS
    when(userStorage.findUserById(userIdInput)).thenThrow(new UserNotFoundException(userIdInput));

    // EJECUCIÓN Y VALIDACIONES
    UserNotFoundException ex =
        assertThrows(
            UserNotFoundException.class,
            () -> selectUserByIdUseCaseImpl.selectUserById(userIdInput));

    assertEquals("No se encuentra el usuario con id: 99", ex.getMessage());
  }
}
