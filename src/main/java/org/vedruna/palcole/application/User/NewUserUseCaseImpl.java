package org.vedruna.palcole.application.User;

import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;

public class NewUserUseCaseImpl implements NewUserUseCase {

  private final RoleStorage roleStorage;

  public NewUserUseCaseImpl(UserStorage roleStorage) {
    this.roleStorage = roleStorage;
  }

  @Override
  public User newRole(User newRole) {
    return roleStorage.insertUser(newRole);
  }
}
