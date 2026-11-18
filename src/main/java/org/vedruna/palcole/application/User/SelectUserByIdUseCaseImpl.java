package org.vedruna.palcole.application.User;

import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.ports.inbound.user.SelectUserByIdUseCase;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;

public class SelectUserByIdUseCaseImpl implements SelectUserByIdUseCase {

  private final UserStorage userStorage;

  public SelectUserByIdUseCaseImpl(UserStorage userStorage) {
    this.userStorage = userStorage;
  }

  @Override
  public User selectUserById(Integer userId) {
    return userStorage.findUserById(userId);
  }
}
