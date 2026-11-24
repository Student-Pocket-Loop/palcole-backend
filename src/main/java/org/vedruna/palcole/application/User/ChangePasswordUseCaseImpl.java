package org.vedruna.palcole.application.User;

import org.vedruna.palcole.domain.ports.inbound.user.ChangePasswordUseCase;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;

public class ChangePasswordUseCaseImpl implements ChangePasswordUseCase {

  private final UserStorage userStorage;

  public ChangePasswordUseCaseImpl(UserStorage userStorage) {
    this.userStorage = userStorage;
  }

  @Override
  public void changePassword(Integer userId, String currentPassword, String newPassword) {
    if (currentPassword.equals(newPassword)) {
      throw new SamePasswordException();
    }
    userStorage.changePassword(userId, currentPassword, newPassword);
  }
}
