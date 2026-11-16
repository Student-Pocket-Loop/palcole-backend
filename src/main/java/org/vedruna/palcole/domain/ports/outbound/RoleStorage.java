package org.vedruna.palcole.domain.ports.outbound;

import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface RoleStorage {
    PageModel<Role> selectAllRoles(PageableModel pageableModel);
    Role findRoleById(Integer roleId);
    Role updateRole(Role newRole);
    Role insertRole(Role newRole);
    void deleteRole(Integer roleId);
}
