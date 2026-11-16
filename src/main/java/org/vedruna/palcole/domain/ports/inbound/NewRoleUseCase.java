package org.vedruna.palcole.domain.ports.inbound;

import org.vedruna.palcole.domain.model.Role;

public interface NewRoleUseCase {
  Role newRole(Role newRole);
}
