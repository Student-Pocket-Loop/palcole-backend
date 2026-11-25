package org.vedruna.palcole.domain.ports.outbound;

import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;

public interface UserStorage {

  PageModel<User> selectAllUsers(PageableModel pageableModel);

  User findUserById(Integer userId);

  User updateUser(Integer userId, User newUser);

  User insertUser(User newUser);

  void deleteUser(Integer userId);

  void changePassword(Integer userId, String currentPassword, String newPassword);
}
