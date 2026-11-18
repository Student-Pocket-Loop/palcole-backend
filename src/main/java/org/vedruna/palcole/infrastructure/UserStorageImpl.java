package org.vedruna.palcole.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.UserOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.exception.UserNotFoundException;
import org.vedruna.palcole.persistance.model.UserEntity;
import org.vedruna.palcole.persistance.repository.UserRepository;

@Component
@AllArgsConstructor
public class UserStorageImpl implements UserStorage {

  private final UserRepository userRepository;
  private final UserOutConverter userOutConverter;

  @Override
  public PageModel<User> selectAllUsers(PageableModel pageableModel) {
    Pageable pageable = userOutConverter.toPageable(pageableModel);
    Page<UserEntity> usersPage = userRepository.findAll(pageable);
    return userOutConverter.toUserPageModel(usersPage);
  }

  @Override
  public User findUserById(Integer userId) {
    UserEntity userEntity =
        userRepository
            .findById(userId)
            .orElseThrow(
                () -> new UserNotFoundException("No se encuentra usuario con id: " + userId));
    return userOutConverter.toDomain(userEntity);
  }

  @Override
  public User updateUser(Integer userId, User newUser) {
    UserEntity userEntity =
        userRepository
            .findById(userId)
            .orElseThrow(
                () -> new UserNotFoundException("No se encuentra usuario con id: " + userId));

    userEntity.setUserId(newUser.getRolesRoleId());
    userEntity.setUsername(newUser.getUsername());
    userEntity.setEmail(newUser.getEmail());
    userEntity.setPassword(newUser.getPassword());
    userEntity.setEducationalCenter(newUser.getEducationalCenter());
    userEntity.setCreateDate(newUser.getCreateDate());
    userEntity.setAccountExpirationDate(newUser.getAccountExpirationDate());
    userEntity.setIsLocked(newUser.isLocked());
    userEntity.setCredentialsExpirationDate(newUser.getCredentialsExpirationDate());
    userEntity.setIsEnabled(newUser.isEnabled());

    return userOutConverter.toDomain(userRepository.save(userEntity));
  }

  @Override
  public User insertUser(User newUser) {
    return userOutConverter.toDomain(userRepository.save(userOutConverter.toEntity(newUser)));
  }

  @Override
  public void deleteUser(Integer userId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException("No se encuentra usuario con id: " + userId);
    }
    userRepository.deleteById(userId);
  }
}