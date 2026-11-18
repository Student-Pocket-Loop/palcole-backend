package org.vedruna.palcole.domain.ports.inbound.role;

import org.vedruna.palcole.domain.model.Role;

public interface SelectRoleByIdUseCase {
  Role selectRoleById(Integer roleId);
}
