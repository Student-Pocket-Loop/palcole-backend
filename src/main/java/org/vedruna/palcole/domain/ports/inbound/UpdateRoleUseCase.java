package org.vedruna.palcole.domain.ports.inbound;

import org.vedruna.palcole.domain.model.Role;

public interface UpdateRoleUseCase {
  Role updateRole(Integer roleId, Role newRole);
}
