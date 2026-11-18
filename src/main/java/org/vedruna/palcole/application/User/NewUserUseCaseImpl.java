package org.vedruna.palcole.application.User;

import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.ports.inbound.user.NewUserUseCase;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;

public class NewUserUseCaseImpl implements NewUserUseCase {

  private final UserStorage userStorage;

  public NewUserUseCaseImpl(UserStorage userStorage) {
    this.userStorage = userStorage;
  }

  @Override
  public User newUser(User newUser) {
    return userStorage.insertUser(newUser);
  }
}
