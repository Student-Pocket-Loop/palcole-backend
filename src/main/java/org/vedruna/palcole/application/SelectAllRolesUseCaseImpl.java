package org.vedruna.palcole.application;

import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.SelectAllRolesUseCase;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;

public class SelectAllRolesUseCaseImpl implements SelectAllRolesUseCase {

  private final RoleStorage roleStorage;

  public SelectAllRolesUseCaseImpl(RoleStorage roleStorage) {
    this.roleStorage = roleStorage;
  }

  @Override
  public PageModel<Role> selectAllRoles(PageableModel pageableModel) {
    return roleStorage.selectAllRoles(pageableModel);
  }
}
