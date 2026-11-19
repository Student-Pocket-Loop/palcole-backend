package org.vedruna.palcole.domain.ports.inbound.user;

import org.vedruna.palcole.domain.model.User;

public interface NewUserUseCase {
  User newUser(User newUser);
}
