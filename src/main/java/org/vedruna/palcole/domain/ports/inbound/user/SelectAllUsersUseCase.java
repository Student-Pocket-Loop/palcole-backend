package org.vedruna.palcole.domain.ports.inbound.user;

import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface SelectAllUsersUseCase {
  PageModel<User> selectAllUsers(PageableModel pageableModel);
}
