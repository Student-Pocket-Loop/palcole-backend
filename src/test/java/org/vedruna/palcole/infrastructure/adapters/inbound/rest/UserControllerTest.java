package org.vedruna.palcole.infrastructure.adapters.inbound.rest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.vedruna.palcole.domain.exception.users.InvalidCurrentPasswordException;
import org.vedruna.palcole.domain.exception.users.SamePasswordException;
import org.vedruna.palcole.domain.exception.users.UserAlreadyExistsException;
import org.vedruna.palcole.domain.exception.users.UserNotFoundException;
import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.user.ChangePasswordUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.DeleteUserUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.NewUserUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.SelectAllUsersUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.SelectUserByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.UpdateUserUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.UserInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.ChangePasswordDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.user.InsertUserDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.user.UserDTO;
import org.vedruna.palcole.persistance.model.UserEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock private NewUserUseCase newUserUseCase;

  @Mock private DeleteUserUseCase deleteUserUseCase;

  @Mock private SelectAllUsersUseCase selectAllUsersUseCase;

  @Mock private SelectUserByIdUseCase selectUserByIdUseCase;

  @Mock private UpdateUserUseCase updateUserUseCase;

  @Mock private ChangePasswordUseCase changePasswordUseCase;

  @Mock private UserInConverter userInConverter;

  @InjectMocks private UserController userController;

  private User crearUser(Integer userId, String username, LocalDateTime fechaFija) {
    User user = new User();
    user.setUserId(userId);
    user.setRolesRoleId(1);
    user.setUsername(username);
    user.setEmail(username.toLowerCase() + "@vedruna.org");
    user.setPassword("password123");
    user.setCreateDate(fechaFija);
    return user;
  }

  private UserDTO crearUserDTO(Integer userId, String username, LocalDateTime fechaFija) {
    UserDTO dto = new UserDTO();
    dto.setUserId(userId);
    dto.setRolesRoleId(1);
    dto.setUsername(username);
    dto.setEmail(username.toLowerCase() + "@vedruna.org");
    dto.setPassword("password123");
    dto.setEducationalCenter("Santa Joaquina de Vedruna");
    dto.setCreateDate(fechaFija);
    dto.setIsLocked(false);
    dto.setIsEnabled(true);
    return dto;
  }

  private InsertUserDTO crearInsertUserDTO(String username) {
    InsertUserDTO dto = new InsertUserDTO();
    dto.setUsername(username);
    dto.setEmail(username.toLowerCase() + "@vedruna.org");
    dto.setPassword("password123");
    return dto;
  }

  @Test
  @DisplayName("Test controlador getAll Ok")
  void getAllOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10);

    // ESPERADO
    List<UserDTO> lista =
        List.of(crearUserDTO(1, "admin", fechaFija), crearUserDTO(2, "juan.perez", fechaFija));

    Pageable pageableEsperado = PageRequest.of(0, 10);

    Page<UserDTO> pageEsperado = new PageImpl<>(lista, pageableEsperado, 2);

    ResponseEntity<Page<UserDTO>> resEsperado = ResponseEntity.ok(pageEsperado);

    // MOCKS
    // CONVERTER MOCK (Pageable -> PageableModel)
    PageableModel pageableModelMock = new PageableModel(0, 10, "UNSORTED");

    when(userInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);

    // USE CASE MOCK
    PageModel<User> pageModelMock =
        new PageModel<>(
            List.of(crearUser(1, "admin", fechaFija), crearUser(2, "juan.perez", fechaFija)),
            2,
            1,
            2,
            10,
            0);

    when(selectAllUsersUseCase.selectAllUsers(pageableModelMock)).thenReturn(pageModelMock);

    // CONVERTER MOCK (PageModel -> Page de DTOs)
    Page<UserDTO> pageDtoMock =
        new PageImpl<>(
            List.of(crearUserDTO(1, "admin", fechaFija), crearUserDTO(2, "juan.perez", fechaFija)),
            PageRequest.of(0, 10),
            2);

    when(userInConverter.toUsersPage(pageModelMock, pageableModelMock)).thenReturn(pageDtoMock);

    // EJECUCIÓN
    ResponseEntity<Page<UserDTO>> res = userController.getAllUsers(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.OK, res.getStatusCode());
          assertEquals(2, res.getBody().getContent().size());
          assertEquals(pageEsperado, res.getBody());
          assertEquals(resEsperado, res);
        });
  }

  @Test
  @DisplayName("Test controlador getAll Ko")
  void getAllKoTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10);

    // MOCKS
    PageableModel pageableModelMock = new PageableModel(0, 10, "UNSORTED");

    when(userInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);
    when(selectAllUsersUseCase.selectAllUsers(pageableModelMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class, () -> userController.getAllUsers(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(userInConverter, never()).toUsersPage(any(), any());
  }

  @Test
  @DisplayName("Test controlador insertIntoUser Ok")
  void insertIntoUserOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    InsertUserDTO insertUserDTOInput = crearInsertUserDTO("juan.perez");

    // ESPERADO
    UserDTO dtoEsperado = crearUserDTO(3, "juan.perez", fechaFija);

    ResponseEntity<UserDTO> resEsperado =
        ResponseEntity.status(HttpStatus.CREATED).body(dtoEsperado);

    // MOCKS
    User userNuevoMock = crearUser(null, "juan.perez", fechaFija);
    User userCreadoMock = crearUser(3, "juan.perez", fechaFija);

    when(userInConverter.toDomainInsert(insertUserDTOInput)).thenReturn(userNuevoMock);
    when(newUserUseCase.newUser(userNuevoMock)).thenReturn(userCreadoMock);
    when(userInConverter.toDto(userCreadoMock))
        .thenReturn(crearUserDTO(3, "juan.perez", fechaFija));

    // EJECUCIÓN
    ResponseEntity<UserDTO> res = userController.insertIntoUser(insertUserDTOInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.CREATED, res.getStatusCode());
          assertEquals(dtoEsperado, res.getBody());
          assertEquals(resEsperado, res);
        });
  }

  @Test
  @DisplayName("Test controlador insertIntoUser Ko")
  void insertIntoUserKoTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    InsertUserDTO insertUserDTOInput = crearInsertUserDTO("admin");

    // MOCKS
    User userNuevoMock = crearUser(null, "admin", fechaFija);

    when(userInConverter.toDomainInsert(insertUserDTOInput)).thenReturn(userNuevoMock);
    when(newUserUseCase.newUser(userNuevoMock)).thenThrow(new UserAlreadyExistsException("admin"));

    // EJECUCIÓN Y VALIDACIONES
    UserAlreadyExistsException ex =
        assertThrows(
            UserAlreadyExistsException.class,
            () -> userController.insertIntoUser(insertUserDTOInput));

    assertEquals("Ya existe un usuario con el nombre: admin", ex.getMessage());
    verify(userInConverter, never()).toDto(any(User.class));
  }

  @Test
  @DisplayName("Test controlador getUserById Ok")
  void getUserByIdOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    Integer userIdInput = 1;

    // ESPERADO
    UserDTO dtoEsperado = crearUserDTO(1, "admin", fechaFija);

    ResponseEntity<UserDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    User userMock = crearUser(1, "admin", fechaFija);

    when(selectUserByIdUseCase.selectUserById(userIdInput)).thenReturn(userMock);
    when(userInConverter.toDto(userMock)).thenReturn(crearUserDTO(1, "admin", fechaFija));

    // EJECUCIÓN
    ResponseEntity<UserDTO> res = userController.getUserById(userIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.OK, res.getStatusCode());
          assertEquals(dtoEsperado, res.getBody());
          assertEquals(resEsperado, res);
        });
  }

  @Test
  @DisplayName("Test controlador getUserById Ko")
  void getUserByIdKoTest() {

    // INPUT
    Integer userIdInput = 99;

    // MOCKS
    when(selectUserByIdUseCase.selectUserById(userIdInput))
        .thenThrow(new UserNotFoundException(userIdInput));

    // EJECUCIÓN Y VALIDACIONES
    UserNotFoundException ex =
        assertThrows(UserNotFoundException.class, () -> userController.getUserById(userIdInput));

    assertEquals("No se encuentra el usuario con id: 99", ex.getMessage());
    verify(userInConverter, never()).toDto(any(User.class));
  }

  @Test
  @DisplayName("Test controlador updateUser Ok")
  void updateUserOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    Integer userIdInput = 1;
    InsertUserDTO insertUserDTOInput = crearInsertUserDTO("maria.garcia");

    // ESPERADO
    UserDTO dtoEsperado = crearUserDTO(1, "maria.garcia", fechaFija);

    ResponseEntity<UserDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    User userNuevosDatosMock = crearUser(null, "maria.garcia", fechaFija);
    User userActualizadoMock = crearUser(1, "maria.garcia", fechaFija);

    when(userInConverter.toDomainInsert(insertUserDTOInput)).thenReturn(userNuevosDatosMock);
    when(updateUserUseCase.updateUser(userIdInput, userNuevosDatosMock))
        .thenReturn(userActualizadoMock);
    when(userInConverter.toDto(userActualizadoMock))
        .thenReturn(crearUserDTO(1, "maria.garcia", fechaFija));

    // EJECUCIÓN
    ResponseEntity<UserDTO> res = userController.updateUser(userIdInput, insertUserDTOInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.OK, res.getStatusCode());
          assertEquals(dtoEsperado, res.getBody());
          assertEquals(resEsperado, res);
        });
  }

  @Test
  @DisplayName("Test controlador updateUser Ko")
  void updateUserKoTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    Integer userIdInput = 99;
    InsertUserDTO insertUserDTOInput = crearInsertUserDTO("maria.garcia");

    // MOCKS
    User userNuevosDatosMock = crearUser(null, "maria.garcia", fechaFija);

    when(userInConverter.toDomainInsert(insertUserDTOInput)).thenReturn(userNuevosDatosMock);
    when(updateUserUseCase.updateUser(userIdInput, userNuevosDatosMock))
        .thenThrow(new UserNotFoundException(userIdInput));

    // EJECUCIÓN Y VALIDACIONES
    UserNotFoundException ex =
        assertThrows(
            UserNotFoundException.class,
            () -> userController.updateUser(userIdInput, insertUserDTOInput));

    assertEquals("No se encuentra el usuario con id: 99", ex.getMessage());
    verify(userInConverter, never()).toDto(any(User.class));
  }

  @Test
  @DisplayName("Test controlador deleteUser Ok")
  void deleteUserOkTest() {

    // INPUT
    Integer userIdInput = 1;

    // ESPERADO
    ResponseEntity<Void> resEsperado = ResponseEntity.noContent().build();

    // MOCKS
    doNothing().when(deleteUserUseCase).deleteUser(userIdInput);

    // EJECUCIÓN
    ResponseEntity<Void> res = userController.deleteUser(userIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
          assertNull(res.getBody());
          assertEquals(resEsperado, res);
        });
    verify(deleteUserUseCase).deleteUser(userIdInput);
  }

  @Test
  @DisplayName("Test controlador deleteUser Ko")
  void deleteUserKoTest() {

    // INPUT
    Integer userIdInput = 99;

    // MOCKS
    doThrow(new UserNotFoundException(userIdInput)).when(deleteUserUseCase).deleteUser(userIdInput);

    // EJECUCIÓN Y VALIDACIONES
    UserNotFoundException ex =
        assertThrows(UserNotFoundException.class, () -> userController.deleteUser(userIdInput));

    assertEquals("No se encuentra el usuario con id: 99", ex.getMessage());
  }

  @Test
  @DisplayName("Test controller changePassword Ok")
  void changePasswordOkTest() {

    // INPUT
    Integer userIdInput = 1;
    UserEntity loggedUserInput = new UserEntity();
    loggedUserInput.setUserId(userIdInput);

    ChangePasswordDTO changePasswordDTOInput = new ChangePasswordDTO();
    changePasswordDTOInput.setCurrentPassword("OldPassword123!");
    changePasswordDTOInput.setNewPassword("NewPassword456!");

    // MOCKS
    doNothing()
        .when(changePasswordUseCase)
        .changePassword(
            userIdInput,
            changePasswordDTOInput.getCurrentPassword(),
            changePasswordDTOInput.getNewPassword());

    // EJECUCIÓN
    ResponseEntity<Void> res =
        userController.changePassword(loggedUserInput, changePasswordDTOInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
          assertNull(res.getBody());
          verify(changePasswordUseCase)
              .changePassword(
                  userIdInput,
                  changePasswordDTOInput.getCurrentPassword(),
                  changePasswordDTOInput.getNewPassword());
        });
  }

  @Test
  @DisplayName("Test controller changePassword Ko SamePasswordException")
  void changePasswordKoSamePasswordTest() {

    // INPUT
    Integer userIdInput = 1;
    UserEntity loggedUserInput = new UserEntity();
    loggedUserInput.setUserId(userIdInput);

    ChangePasswordDTO changePasswordDTOInput = new ChangePasswordDTO();
    changePasswordDTOInput.setCurrentPassword("SamePassword123!");
    changePasswordDTOInput.setNewPassword("SamePassword123!");

    // MOCKS
    doThrow(new SamePasswordException())
        .when(changePasswordUseCase)
        .changePassword(
            userIdInput,
            changePasswordDTOInput.getCurrentPassword(),
            changePasswordDTOInput.getNewPassword());

    // EJECUCIÓN Y VALIDACIONES
    assertAll(
        () -> {
          assertThrows(
              SamePasswordException.class,
              () -> userController.changePassword(loggedUserInput, changePasswordDTOInput));
          verify(changePasswordUseCase)
              .changePassword(
                  userIdInput,
                  changePasswordDTOInput.getCurrentPassword(),
                  changePasswordDTOInput.getNewPassword());
        });
  }

  @Test
  @DisplayName("Test controller changePassword Ko InvalidCurrentPasswordException")
  void changePasswordKoInvalidCurrentPasswordTest() {

    // INPUT
    Integer userIdInput = 1;
    UserEntity loggedUserInput = new UserEntity();
    loggedUserInput.setUserId(userIdInput);

    ChangePasswordDTO changePasswordDTOInput = new ChangePasswordDTO();
    changePasswordDTOInput.setCurrentPassword("WrongPassword123!");
    changePasswordDTOInput.setNewPassword("NewPassword456!");

    // MOCKS
    doThrow(new InvalidCurrentPasswordException())
        .when(changePasswordUseCase)
        .changePassword(
            userIdInput,
            changePasswordDTOInput.getCurrentPassword(),
            changePasswordDTOInput.getNewPassword());

    // EJECUCIÓN Y VALIDACIONES
    assertAll(
        () -> {
          assertThrows(
              InvalidCurrentPasswordException.class,
              () -> userController.changePassword(loggedUserInput, changePasswordDTOInput));
          verify(changePasswordUseCase)
              .changePassword(
                  userIdInput,
                  changePasswordDTOInput.getCurrentPassword(),
                  changePasswordDTOInput.getNewPassword());
        });
  }
}
