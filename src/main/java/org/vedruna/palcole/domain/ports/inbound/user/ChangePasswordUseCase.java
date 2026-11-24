package org.vedruna.palcole.domain.ports.inbound.user;

public interface ChangePasswordUseCase {
  void changePassword(Integer userId, String currentPassword, String newPassword);
}
