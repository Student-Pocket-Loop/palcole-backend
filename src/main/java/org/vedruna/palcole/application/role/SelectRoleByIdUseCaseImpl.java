package org.vedruna.palcole.application.role;

import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.ports.inbound.SelectRoleByIdUseCase;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;

public class SelectRoleByIdUseCaseImpl implements SelectRoleByIdUseCase {

  private final RoleStorage roleStorage;

  public SelectRoleByIdUseCaseImpl(RoleStorage roleStorage) {
    this.roleStorage = roleStorage;
  }

  @Override
  public Role selectRoleById(Integer roleId) {
    return roleStorage.findRoleById(roleId);
  }
}
