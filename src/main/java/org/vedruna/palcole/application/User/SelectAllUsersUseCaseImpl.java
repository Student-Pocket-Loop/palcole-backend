package org.vedruna.palcole.application.User;

import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.user.SelectAllUsersUseCase;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;

public class SelectAllUsersUseCaseImpl implements SelectAllUsersUseCase {

  private final UserStorage userStorage;

  public SelectAllUsersUseCaseImpl(UserStorage userStorage) {
    this.userStorage = userStorage;
  }

  @Override
  public PageModel<User> selectAllUsers(PageableModel pageableModel) {
    return userStorage.selectAllUsers(pageableModel);
  }
}
