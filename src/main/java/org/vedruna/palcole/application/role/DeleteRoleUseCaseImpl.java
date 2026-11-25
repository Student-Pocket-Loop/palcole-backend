package org.vedruna.palcole.application.role;

import org.vedruna.palcole.domain.ports.inbound.role.DeleteRoleUseCase;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;

public class DeleteRoleUseCaseImpl implements DeleteRoleUseCase {

  private final RoleStorage roleStorage;

  public DeleteRoleUseCaseImpl(RoleStorage roleStorage) {
    this.roleStorage = roleStorage;
  }

  @Override
  public void deleteRole(Integer roleId) {
    roleStorage.deleteRole(roleId);
  }
}
