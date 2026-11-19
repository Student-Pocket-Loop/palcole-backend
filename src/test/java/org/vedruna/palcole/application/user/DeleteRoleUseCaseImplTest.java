package org.vedruna.palcole.application.user;

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
import org.vedruna.palcole.application.User.DeleteUserUseCaseImpl;
import org.vedruna.palcole.domain.exception.users.UserNotFoundException;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseImplTest {

  @Mock private UserStorage userStorage;

  @InjectMocks private DeleteUserUseCaseImpl deleteUserUseCaseImpl;

  @Test
  @DisplayName("Test caso de uso deleteUser Ok")
  void deleteUserOkTest() {

    // INPUT
    Integer userIdInput = 1;

    // MOCKS
    doNothing().when(userStorage).deleteUser(userIdInput);

    // EJECUCIÓN Y VALIDACIONES
    assertDoesNotThrow(() -> deleteUserUseCaseImpl.deleteUser(userIdInput));
    verify(userStorage).deleteUser(userIdInput);
  }

  @Test
  @DisplayName("Test caso de uso deleteUser Ko")
  void deleteUserKoTest() {

    // INPUT
    Integer userIdInput = 99;

    // MOCKS
    doThrow(new UserNotFoundException(userIdInput)).when(userStorage).deleteUser(userIdInput);

    // EJECUCIÓN Y VALIDACIONES
    UserNotFoundException ex =
        assertThrows(
            UserNotFoundException.class, () -> deleteUserUseCaseImpl.deleteUser(userIdInput));

    assertEquals("No se encuentra el usuario con id: 99", ex.getMessage());
  }
}
