package org.vedruna.palcole.application.User;

import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public class SelectAllUsersUseCaseImpl implements SelectAllRolesUseCase {

  private final UserStorage userStorage;

  public SelectAllUsersUseCaseImpl(RoleStorage roleStorage) {
    this.userStorage = roleStorage;
  }

  @Override
  public PageModel<Role> selectAllUsers(PageableModel pageableModel) {
    return userStorage.selectAllRoles(pageableModel);
  }
}
