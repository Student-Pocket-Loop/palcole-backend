package org.vedruna.palcole.infrastructure;

import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;

public class RoleStorageImpl implements RoleStorage {

  @Override
  public PageModel<Role> selectAllRoles(PageableModel pageableModel) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'selectAllRoles'");
  }

  @Override
  public Role findRoleById(Integer roleId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findRoleById'");
  }

  @Override
  public Role updateRole(Integer roleId, Role newRole) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateRole'");
  }

  @Override
  public Role insertRole(Role newRole) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'insertRole'");
  }

  @Override
  public void deleteRole(Integer roleId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteRole'");
  }
}
