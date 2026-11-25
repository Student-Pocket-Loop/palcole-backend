package org.vedruna.palcole.application.user;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.application.User.ChangePasswordUseCaseImpl;
import org.vedruna.palcole.domain.exception.users.SamePasswordException;
import org.vedruna.palcole.domain.exception.users.UserNotFoundException;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;

@ExtendWith(MockitoExtension.class)
class ChangePasswordUseCaseImplTest {

  @Mock private UserStorage userStorage;

  @InjectMocks private ChangePasswordUseCaseImpl changePasswordUseCaseImpl;

  @Test
  @DisplayName("Test use case changePassword Ok")
  void changePasswordOkTest() {

    // INPUT
    Integer userIdInput = 1;
    String currentPasswordInput = "OldPassword123!";
    String newPasswordInput = "NewPassword456!";

    // MOCKS
    doNothing()
        .when(userStorage)
        .changePassword(userIdInput, currentPasswordInput, newPasswordInput);

    // EJECUCIÓN Y VALIDACIONES
    assertAll(
        () -> {
          assertDoesNotThrow(
              () ->
                  changePasswordUseCaseImpl.changePassword(
                      userIdInput, currentPasswordInput, newPasswordInput));
          verify(userStorage).changePassword(userIdInput, currentPasswordInput, newPasswordInput);
        });
  }

  @Test
  @DisplayName("Test use case changePassword Ko SamePasswordException")
  void changePasswordKoSamePasswordTest() {

    // INPUT
    Integer userIdInput = 1;
    String currentPasswordInput = "SamePassword123!";
    String newPasswordInput = "SamePassword123!";

    // EJECUCIÓN Y VALIDACIONES
    assertAll(
        () -> {
          assertThrows(
              SamePasswordException.class,
              () ->
                  changePasswordUseCaseImpl.changePassword(
                      userIdInput, currentPasswordInput, newPasswordInput));
          verify(userStorage, never())
              .changePassword(userIdInput, currentPasswordInput, newPasswordInput);
        });
  }

  @Test
  @DisplayName("Test use case changePassword Ko UserNotFoundException")
  void changePasswordKoUserNotFoundTest() {

    // INPUT
    Integer userIdInput = 99;
    String currentPasswordInput = "OldPassword123!";
    String newPasswordInput = "NewPassword456!";

    // MOCKS
    doThrow(new UserNotFoundException(userIdInput))
        .when(userStorage)
        .changePassword(userIdInput, currentPasswordInput, newPasswordInput);

    // EJECUCIÓN Y VALIDACIONES
    assertAll(
        () -> {
          assertThrows(
              UserNotFoundException.class,
              () ->
                  changePasswordUseCaseImpl.changePassword(
                      userIdInput, currentPasswordInput, newPasswordInput));
          verify(userStorage).changePassword(userIdInput, currentPasswordInput, newPasswordInput);
        });
  }
}
