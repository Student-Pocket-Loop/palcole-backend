package org.vedruna.palcole.infrastructure;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.vedruna.palcole.domain.exception.roles.RoleNotFoundException;
import org.vedruna.palcole.domain.exception.users.InvalidCurrentPasswordException;
import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.UserStorage;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.UserOutConverter;
import org.vedruna.palcole.persistance.exception.UserNotFoundException;
import org.vedruna.palcole.persistance.model.RoleEntity;
import org.vedruna.palcole.persistance.model.UserEntity;
import org.vedruna.palcole.persistance.repository.RoleRepository;
import org.vedruna.palcole.persistance.repository.UserRepository;

/**
 * Adaptador de salida de los usuarios. Reutiliza la {@link UserEntity} y el {@link UserRepository}
 * de la capa persistance. Es el único sitio donde se codifica la contraseña antes de guardarla.
 */
@Component
@AllArgsConstructor
public class UserStorageImpl implements UserStorage {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserOutConverter userOutConverter;
  private final PasswordEncoder passwordEncoder;

  @Override
  public PageModel<User> selectAllUsers(PageableModel pageableModel) {
    Pageable pageable = userOutConverter.toPageable(pageableModel);
    Page<UserEntity> userPage = userRepository.findAll(pageable);
    return userOutConverter.toUserPageModel(userPage);
  }

  @Override
  public User findUserById(Integer userId) {
    UserEntity userEntity =
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    return userOutConverter.toDomain(userEntity);
  }

  @Override
  public User updateUser(Integer userId, User newUser) {
    UserEntity userEntity =
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

    userEntity.setUsername(newUser.getUsername());
    userEntity.setEmail(newUser.getEmail());
    if (newUser.getEducationalCenter() != null) {
      userEntity.setEducationalCenter(newUser.getEducationalCenter());
    }
    if (newUser.getAccountExpirationDate() != null) {
      userEntity.setAccountExpirationDate(newUser.getAccountExpirationDate());
    }
    if (newUser.getCredentialsExpirationDate() != null) {
      userEntity.setCredentialsExpirationDate(newUser.getCredentialsExpirationDate());
    }

    userEntity.setIsLocked(newUser.getIsLocked());

    userEntity.setIsEnabled(newUser.getIsEnabled());

    if (newUser.getRolesRoleId() != null) {
      userEntity.setUserRol(findRole(newUser.getRolesRoleId()));
    }
    if (newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
      userEntity.setPassword(passwordEncoder.encode(newUser.getPassword()));
    }

    return userOutConverter.toDomain(userRepository.save(userEntity));
  }

  @Override
  public User insertUser(User newUser) {
    UserEntity userEntity = userOutConverter.toEntity(newUser);
    userEntity.setPassword(passwordEncoder.encode(newUser.getPassword()));

    Integer roleId = (newUser.getRolesRoleId() != null) ? newUser.getRolesRoleId() : 2;
    userEntity.setUserRol(findRole(roleId));

    LocalDateTime neverExpires = LocalDateTime.of(2999, 12, 31, 23, 59, 59);

    userEntity.setCreateDate(LocalDateTime.now());
    userEntity.setAccountExpirationDate(neverExpires); // Opcional, para que no quede NULL
    userEntity.setCredentialsExpirationDate(neverExpires);

    return userOutConverter.toDomain(userRepository.save(userEntity));
  }

  @Override
  public void deleteUser(Integer userId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException(userId);
    }
    userRepository.deleteById(userId);
  }

  private RoleEntity findRole(Integer roleId) {
    return roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException(roleId));
  }

  @Override
  public void changePassword(Integer userId, String currentPassword, String newPassword) {
    UserEntity userEntity =
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

    if (!passwordEncoder.matches(currentPassword, userEntity.getPassword())) {
      throw new InvalidCurrentPasswordException();
    }

    userEntity.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(userEntity);
  }
}
