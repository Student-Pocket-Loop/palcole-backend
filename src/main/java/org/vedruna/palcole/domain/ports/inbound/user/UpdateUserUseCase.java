package org.vedruna.palcole.domain.ports.inbound.user;

import org.vedruna.palcole.domain.model.User;

public interface UpdateUserUseCase {
  User updateUser(Integer userId, User newUser);
}
