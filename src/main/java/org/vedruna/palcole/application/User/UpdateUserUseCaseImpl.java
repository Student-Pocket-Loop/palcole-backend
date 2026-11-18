package org.vedruna.palcole.application.User;

import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.ports.inbound.UpdateUserUseCase;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;

public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

  private final UserStorage userStorage;

  public UpdateUserUseCaseImpl(UserStorage userStorage) {
    this.userStorage = userStorage;
  }

  @Override
  public User updateUser(Integer userId, User newUser) {
    return userStorage.updateUser(userId, newUser);
  }
}
