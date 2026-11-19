package org.vedruna.palcole.application.role;

import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.ports.inbound.role.UpdateRoleUseCase;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;

public class UpdateRoleUseCaseImpl implements UpdateRoleUseCase {

  private final RoleStorage roleStorage;

  public UpdateRoleUseCaseImpl(RoleStorage roleStorage) {
    this.roleStorage = roleStorage;
  }

  @Override
  public Role updateRole(Integer roleId, Role newRole) {
    return roleStorage.updateRole(roleId, newRole);
  }
}
