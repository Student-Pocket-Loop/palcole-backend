package org.vedruna.palcole.application;

import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.ports.inbound.NewRoleUseCase;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;

public class NewRoleUseCaseImpl implements NewRoleUseCase {

  private final RoleStorage roleStorage;

  public NewRoleUseCaseImpl(RoleStorage roleStorage) {
    this.roleStorage = roleStorage;
  }

  @Override
  public Role newRole(Role newRole) {
    return roleStorage.insertRole(newRole);
  }
}
