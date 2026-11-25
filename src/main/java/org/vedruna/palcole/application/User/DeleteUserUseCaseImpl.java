package org.vedruna.palcole.application.User;

import org.vedruna.palcole.domain.ports.inbound.user.DeleteUserUseCase;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;

public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

  private final UserStorage userStorage;

  public DeleteUserUseCaseImpl(UserStorage userStorage) {
    this.userStorage = userStorage;
  }

  @Override
  public void deleteUser(Integer userId) {
    userStorage.deleteUser(userId);
  }
}
