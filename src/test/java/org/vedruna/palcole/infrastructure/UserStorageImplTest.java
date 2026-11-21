package org.vedruna.palcole.infrastructure;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vedruna.palcole.domain.exception.roles.RoleNotFoundException;
import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.UserOutConverter;
import org.vedruna.palcole.persistance.exception.UserNotFoundException;
import org.vedruna.palcole.persistance.model.RoleEntity;
import org.vedruna.palcole.persistance.model.UserEntity;
import org.vedruna.palcole.persistance.repository.RoleRepository;
import org.vedruna.palcole.persistance.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserStorageImplTest {

  @Mock private UserRepository userRepository;

  @Mock private RoleRepository roleRepository;

  @Mock private UserOutConverter userOutConverter;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UserStorageImpl userStorageImpl;

  private User crearUser(
      Integer userId, String username, String email, String password, Integer rolesRoleId) {
    User user = new User();
    user.setUserId(userId);
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    user.setEducationalCenter("Vedruna");
    user.setAccountExpirationDate(LocalDateTime.of(2027, 12, 31, 23, 59));
    user.setCredentialsExpirationDate(LocalDateTime.of(2027, 6, 30, 23, 59));
    user.setIsLocked(false);
    user.setIsEnabled(true);
    user.setRolesRoleId(rolesRoleId);
    return user;
  }

  private UserEntity crearUserEntity(
      Integer userId, String username, String email, String password, RoleEntity roleEntity) {
    UserEntity entity = new UserEntity();
    entity.setUserId(userId);
    entity.setUsername(username);
    entity.setEmail(email);
    entity.setPassword(password);
    entity.setEducationalCenter("Vedruna");
    entity.setAccountExpirationDate(LocalDateTime.of(2027, 12, 31, 23, 59));
    entity.setCredentialsExpirationDate(LocalDateTime.of(2027, 6, 30, 23, 59));
    entity.setIsLocked(false);
    entity.setIsEnabled(true);
    entity.setUserRol(roleEntity);
    return entity;
  }

  private RoleEntity crearRoleEntity(Integer roleId, String name) {
    RoleEntity roleEntity = new RoleEntity();
    roleEntity.setUserId(roleId);
    roleEntity.setName(name);
    return roleEntity;
  }

  @Test
  @DisplayName("Test storage selectAllUsers Ok")
  void selectAllUsersOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<User> pageModelEsperado =
        new PageModel<>(
            List.of(
                crearUser(1, "jdoe", "jdoe@vedruna.es", "encoded1", 1),
                crearUser(2, "asmith", "asmith@vedruna.es", "encoded2", 2)),
            2,
            1,
            2,
            10,
            0);

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);
    RoleEntity roleAdminMock = crearRoleEntity(1, "ADMIN");
    RoleEntity roleUserMock = crearRoleEntity(2, "USER");

    Page<UserEntity> pageEntityMock =
        new PageImpl<>(
            List.of(
                crearUserEntity(1, "jdoe", "jdoe@vedruna.es", "encoded1", roleAdminMock),
                crearUserEntity(2, "asmith", "asmith@vedruna.es", "encoded2", roleUserMock)),
            pageableMock,
            2);

    PageModel<User> pageModelMock =
        new PageModel<>(
            List.of(
                crearUser(1, "jdoe", "jdoe@vedruna.es", "encoded1", 1),
                crearUser(2, "asmith", "asmith@vedruna.es", "encoded2", 2)),
            2,
            1,
            2,
            10,
            0);

    when(userOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(userRepository.findAll(pageableMock)).thenReturn(pageEntityMock);
    when(userOutConverter.toUserPageModel(pageEntityMock)).thenReturn(pageModelMock);

    // EJECUCIÓN
    PageModel<User> res = userStorageImpl.selectAllUsers(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage selectAllUsers Ko")
  void selectAllUsersKoTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "INVALID_SORT");

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);

    when(userOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(userRepository.findAll(pageableMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> userStorageImpl.selectAllUsers(pageableModelInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(userOutConverter, never()).toUserPageModel(any());
  }

  @Test
  @DisplayName("Test storage findUserById Ok")
  void findUserByIdOkTest() {

    // INPUT
    Integer userIdInput = 1;

    // ESPERADO
    User userEsperado = crearUser(1, "jdoe", "jdoe@vedruna.es", "encodedPass", 1);

    // MOCKS
    RoleEntity roleEntityMock = crearRoleEntity(1, "ADMIN");
    UserEntity userEntityMock =
        crearUserEntity(1, "jdoe", "jdoe@vedruna.es", "encodedPass", roleEntityMock);
    User userMock = crearUser(1, "jdoe", "jdoe@vedruna.es", "encodedPass", 1);

    when(userRepository.findById(userIdInput)).thenReturn(Optional.of(userEntityMock));
    when(userOutConverter.toDomain(userEntityMock)).thenReturn(userMock);

    // EJECUCIÓN
    User res = userStorageImpl.findUserById(userIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(userEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage findUserById Ko")
  void findUserByIdKoTest() {

    // INPUT
    Integer userIdInput = 99;

    // MOCKS
    when(userRepository.findById(userIdInput)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(UserNotFoundException.class, () -> userStorageImpl.findUserById(userIdInput));

    verify(userOutConverter, never()).toDomain(any(UserEntity.class));
  }

  @Test
  @DisplayName("Test storage updateUser Ok")
  void updateUserOkTest() {

    // INPUT
    Integer userIdInput = 1;
    User newUserInput = crearUser(null, "jdoe_updated", "new@vedruna.es", "newPlainPass", 2);

    // ESPERADO
    User userEsperado = crearUser(1, "jdoe_updated", "new@vedruna.es", "newEncodedPass", 2);

    // MOCKS
    RoleEntity roleAnteriorMock = crearRoleEntity(1, "ADMIN");
    RoleEntity roleNuevoMock = crearRoleEntity(2, "TEACHER");
    UserEntity userEntityExistenteMock =
        crearUserEntity(1, "jdoe", "jdoe@vedruna.es", "oldEncodedPass", roleAnteriorMock);
    UserEntity userEntityGuardadoMock =
        crearUserEntity(1, "jdoe_updated", "new@vedruna.es", "newEncodedPass", roleNuevoMock);
    User userActualizadoMock = crearUser(1, "jdoe_updated", "new@vedruna.es", "newEncodedPass", 2);

    when(userRepository.findById(userIdInput)).thenReturn(Optional.of(userEntityExistenteMock));
    when(roleRepository.findById(2)).thenReturn(Optional.of(roleNuevoMock));
    when(passwordEncoder.encode("newPlainPass")).thenReturn("newEncodedPass");
    when(userRepository.save(userEntityExistenteMock)).thenReturn(userEntityGuardadoMock);
    when(userOutConverter.toDomain(userEntityGuardadoMock)).thenReturn(userActualizadoMock);

    // EJECUCIÓN
    User res = userStorageImpl.updateUser(userIdInput, newUserInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals("jdoe_updated", userEntityExistenteMock.getUsername());
          assertEquals("new@vedruna.es", userEntityExistenteMock.getEmail());
          assertEquals("newEncodedPass", userEntityExistenteMock.getPassword());
          assertEquals(roleNuevoMock, userEntityExistenteMock.getUserRol());
          assertEquals(userEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage updateUser Ko UserNotFound")
  void updateUserKoUserNotFoundTest() {

    // INPUT
    Integer userIdInput = 99;
    User newUserInput = crearUser(null, "jdoe_updated", "new@vedruna.es", "newPlainPass", 2);

    // MOCKS
    when(userRepository.findById(userIdInput)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(
        UserNotFoundException.class, () -> userStorageImpl.updateUser(userIdInput, newUserInput));

    verify(roleRepository, never()).findById(any());
    verify(passwordEncoder, never()).encode(anyString());
    verify(userRepository, never()).save(any(UserEntity.class));
    verify(userOutConverter, never()).toDomain(any(UserEntity.class));
  }

  @Test
  @DisplayName("Test storage updateUser Ko RoleNotFound")
  void updateUserKoRoleNotFoundTest() {

    // INPUT
    Integer userIdInput = 1;
    User newUserInput = crearUser(null, "jdoe_updated", "new@vedruna.es", "newPlainPass", 99);

    // MOCKS
    RoleEntity roleAnteriorMock = crearRoleEntity(1, "ADMIN");
    UserEntity userEntityExistenteMock =
        crearUserEntity(1, "jdoe", "jdoe@vedruna.es", "oldEncodedPass", roleAnteriorMock);

    when(userRepository.findById(userIdInput)).thenReturn(Optional.of(userEntityExistenteMock));
    when(roleRepository.findById(99)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    RoleNotFoundException ex =
        assertThrows(
            RoleNotFoundException.class,
            () -> userStorageImpl.updateUser(userIdInput, newUserInput));

    assertEquals("No se encuentra el role con id: 99", ex.getMessage());
    verify(passwordEncoder, never()).encode(anyString());
    verify(userRepository, never()).save(any(UserEntity.class));
    verify(userOutConverter, never()).toDomain(any(UserEntity.class));
  }

  @Test
  @DisplayName("Test storage insertUser Ok")
  void insertUserOkTest() {

    // INPUT
    User newUserInput = crearUser(null, "mgarcia", "mgarcia@vedruna.es", "plainPass", 2);

    // ESPERADO
    User userEsperado = crearUser(3, "mgarcia", "mgarcia@vedruna.es", "encodedPass", 2);

    // MOCKS
    RoleEntity roleEntityMock = crearRoleEntity(2, "TEACHER");
    UserEntity userEntityNuevoMock =
        crearUserEntity(null, "mgarcia", "mgarcia@vedruna.es", "plainPass", null);
    UserEntity userEntityGuardadoMock =
        crearUserEntity(3, "mgarcia", "mgarcia@vedruna.es", "encodedPass", roleEntityMock);
    User userCreadoMock = crearUser(3, "mgarcia", "mgarcia@vedruna.es", "encodedPass", 2);

    when(userOutConverter.toEntity(newUserInput)).thenReturn(userEntityNuevoMock);
    when(passwordEncoder.encode("plainPass")).thenReturn("encodedPass");
    when(roleRepository.findById(2)).thenReturn(Optional.of(roleEntityMock));
    when(userRepository.save(userEntityNuevoMock)).thenReturn(userEntityGuardadoMock);
    when(userOutConverter.toDomain(userEntityGuardadoMock)).thenReturn(userCreadoMock);

    // EJECUCIÓN
    User res = userStorageImpl.insertUser(newUserInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals("encodedPass", userEntityNuevoMock.getPassword());
          assertEquals(roleEntityMock, userEntityNuevoMock.getUserRol());
          assertEquals(userEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage insertUser Ko")
  void insertUserKoTest() {

    // INPUT
    User newUserInput = crearUser(null, "mgarcia", "mgarcia@vedruna.es", "plainPass", 99);

    // MOCKS
    UserEntity userEntityNuevoMock =
        crearUserEntity(null, "mgarcia", "mgarcia@vedruna.es", "plainPass", null);

    when(userOutConverter.toEntity(newUserInput)).thenReturn(userEntityNuevoMock);
    when(passwordEncoder.encode("plainPass")).thenReturn("encodedPass");
    when(roleRepository.findById(99)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    RoleNotFoundException ex =
        assertThrows(RoleNotFoundException.class, () -> userStorageImpl.insertUser(newUserInput));

    assertEquals("No se encuentra el role con id: 99", ex.getMessage());
    verify(userRepository, never()).save(any(UserEntity.class));
    verify(userOutConverter, never()).toDomain(any(UserEntity.class));
  }

  @Test
  @DisplayName("Test storage deleteUser Ok")
  void deleteUserOkTest() {

    // INPUT
    Integer userIdInput = 1;

    // MOCKS
    when(userRepository.existsById(userIdInput)).thenReturn(true);
    doNothing().when(userRepository).deleteById(userIdInput);

    // EJECUCIÓN
    userStorageImpl.deleteUser(userIdInput);

    // VALIDACIONES
    verify(userRepository).existsById(userIdInput);
    verify(userRepository).deleteById(userIdInput);
  }

  @Test
  @DisplayName("Test storage deleteUser Ko")
  void deleteUserKoTest() {

    // INPUT
    Integer userIdInput = 99;

    // MOCKS
    when(userRepository.existsById(userIdInput)).thenReturn(false);

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(UserNotFoundException.class, () -> userStorageImpl.deleteUser(userIdInput));

    verify(userRepository, never()).deleteById(any());
  }
}
