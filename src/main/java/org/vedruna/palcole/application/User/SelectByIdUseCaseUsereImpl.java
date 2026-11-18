package org.vedruna.palcole.application.User;

import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.ports.inbound.SelectUserByIdUseCase;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;

public class SelectByIdUseCaseUsereImpl implements SelectUserByIdUseCase {

  private final UserStorage userStorage;

  public SelectByIdUseCasUsereImpl(UserStorage userStorage) {
    this.userStorage = userStorage;
  }

  @Override
  public User selectUserById(Integer userId) {
    return userStorage.findUserById(userId);
  }
}
