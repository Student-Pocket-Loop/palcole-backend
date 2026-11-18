package org.vedruna.palcole.application.User;

public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

  private final UserStorage userStorage;

  public DeleteUserUseCaseImpl(UserStorage userStorage) {
    this.userStorage = userStorage;
  }

  @Override
  public void deleteUser(Integer userId) {
    userStorage.deleteUser(userId);
  }
}
