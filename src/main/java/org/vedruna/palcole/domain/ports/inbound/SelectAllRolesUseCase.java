package org.vedruna.palcole.domain.ports.inbound;

import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface SelectAllRolesUseCase {
    PageModel<Role> selectAllRoles(PageableModel pageableModel);
}
