package org.vedruna.palcole.infrastructure.adapters;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.UserStorageImpl;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.UserOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.exception.UserNotFoundException;
import org.vedruna.palcole.persistance.model.UserEntity;
import org.vedruna.palcole.persistance.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserStorageImplTest {

  @Mock private UserRepository userRepository;

  @Mock private UserOutConverter userOutConverter;

  @InjectMocks private UserStorageImpl userStorageImpl;

  private User crearUser(Integer userId, String username, LocalDateTime fechaFija) {
    User user = new User();
    user.setUserId(userId);
    user.setRolesRoleId(1);
    user.setUsername(username);
    user.setEmail(username.toLowerCase() + "@vedruna.org");
    user.setPassword("password123");
    user.setEducationalCenter("Santa Joaquina de Vedruna");
    user.setCreateDate(fechaFija);
    user.setLocked(false);
    user.setEnabled(true);
    return user;
  }

  private UserEntity crearUserEntity(Integer userId, String username, LocalDateTime fechaFija) {
    UserEntity entity = new UserEntity();
    entity.setUserId(userId);
    entity.setUsername(username);
    entity.setEmail(username.toLowerCase() + "@vedruna.org");
    entity.setPassword("password123");
    entity.setEducationalCenter("Santa Joaquina de Vedruna");
    entity.setCreateDate(fechaFija);
    entity.setIsLocked(false);
    entity.setIsEnabled(true);
    return entity;
  }

  @Test
  @DisplayName("Test storage selectAllUsers Ok")
  void selectAllUsersOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<User> pageModelEsperado =
        new PageModel<>(
            List.of(crearUser(1, "admin", fechaFija), crearUser(2, "juan.perez", fechaFija)),
            2,
            1,
            2,
            10,
            0);

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);
    Page<UserEntity> pageEntityMock =
        new PageImpl<>(
            List.of(
                crearUserEntity(1, "admin", fechaFija),
                crearUserEntity(2, "juan.perez", fechaFija)),
            pageableMock,
            2);
    PageModel<User> pageModelMock =
        new PageModel<>(
            List.of(crearUser(1, "admin", fechaFija), crearUser(2, "juan.perez", fechaFija)),
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

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    Integer userIdInput = 1;

    // ESPERADO
    User userEsperado = crearUser(1, "admin", fechaFija);

    // MOCKS
    UserEntity userEntityMock = crearUserEntity(1, "admin", fechaFija);
    User userMock = crearUser(1, "admin", fechaFija);

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
    UserNotFoundException ex =
        assertThrows(UserNotFoundException.class, () -> userStorageImpl.findUserById(userIdInput));

    assertEquals("No se encuentra usuario con id: 99", ex.getMessage());
    verify(userOutConverter, never()).toDomain(any(UserEntity.class));
  }

  @Test
  @DisplayName("Test storage updateUser Ok")
  void updateUserOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    Integer userIdInput = 1;
    User newUserInput = crearUser(null, "maria.garcia", fechaFija);

    // ESPERADO
    User userEsperado = crearUser(1, "maria.garcia", fechaFija);

    // MOCKS
    UserEntity userEntityExistenteMock = crearUserEntity(1, "admin", fechaFija);
    UserEntity userEntityGuardadoMock = crearUserEntity(1, "maria.garcia", fechaFija);
    User userActualizadoMock = crearUser(1, "maria.garcia", fechaFija);

    when(userRepository.findById(userIdInput)).thenReturn(Optional.of(userEntityExistenteMock));
    when(userRepository.save(userEntityExistenteMock)).thenReturn(userEntityGuardadoMock);
    when(userOutConverter.toDomain(userEntityGuardadoMock)).thenReturn(userActualizadoMock);

    // EJECUCIÓN
    User res = userStorageImpl.updateUser(userIdInput, newUserInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals("maria.garcia", userEntityExistenteMock.getUsername());
          assertEquals("maria.garcia@vedruna.org", userEntityExistenteMock.getEmail());
          assertEquals(userEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage updateUser Ko")
  void updateUserKoTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    Integer userIdInput = 99;
    User newUserInput = crearUser(null, "maria.garcia", fechaFija);

    // MOCKS
    when(userRepository.findById(userIdInput)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    UserNotFoundException ex =
        assertThrows(
            UserNotFoundException.class,
            () -> userStorageImpl.updateUser(userIdInput, newUserInput));

    assertEquals("No se encuentra usuario con id: 99", ex.getMessage());
    verify(userRepository, never()).save(any(UserEntity.class));
    verify(userOutConverter, never()).toDomain(any(UserEntity.class));
  }

  @Test
  @DisplayName("Test storage insertUser Ok")
  void insertUserOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    User newUserInput = crearUser(null, "juan.perez", fechaFija);

    // ESPERADO
    User userEsperado = crearUser(3, "juan.perez", fechaFija);

    // MOCKS
    UserEntity userEntityNuevoMock = crearUserEntity(null, "juan.perez", fechaFija);
    UserEntity userEntityGuardadoMock = crearUserEntity(3, "juan.perez", fechaFija);
    User userCreadoMock = crearUser(3, "juan.perez", fechaFija);

    when(userOutConverter.toEntity(newUserInput)).thenReturn(userEntityNuevoMock);
    when(userRepository.save(userEntityNuevoMock)).thenReturn(userEntityGuardadoMock);
    when(userOutConverter.toDomain(userEntityGuardadoMock)).thenReturn(userCreadoMock);

    // EJECUCIÓN
    User res = userStorageImpl.insertUser(newUserInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(userEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage insertUser Ko")
  void insertUserKoTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    User newUserInput = crearUser(null, "admin", fechaFija);

    // MOCKS
    UserEntity userEntityNuevoMock = crearUserEntity(null, "admin", fechaFija);

    when(userOutConverter.toEntity(newUserInput)).thenReturn(userEntityNuevoMock);
    when(userRepository.save(userEntityNuevoMock))
        .thenThrow(new DataIntegrityViolationException("El usuario ya existe en base de datos"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class, () -> userStorageImpl.insertUser(newUserInput));

    assertEquals("El usuario ya existe en base de datos", ex.getMessage());
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
    UserNotFoundException ex =
        assertThrows(UserNotFoundException.class, () -> userStorageImpl.deleteUser(userIdInput));

    assertEquals("No se encuentra usuario con id: 99", ex.getMessage());
    verify(userRepository, never()).deleteById(any());
  }
}