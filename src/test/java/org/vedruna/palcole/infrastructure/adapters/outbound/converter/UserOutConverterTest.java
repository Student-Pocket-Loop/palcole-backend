package org.vedruna.palcole.infrastructure.adapters.outbound.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.persistance.model.RoleEntity;
import org.vedruna.palcole.persistance.model.UserEntity;

class UserOutConverterTest {

  private final UserOutConverter userOutConverter = Mappers.getMapper(UserOutConverter.class);

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

  private UserEntity crearUserEntity(Integer userId, String username, LocalDateTime fechaFija) {
    RoleEntity role = new RoleEntity();
    role.setUserId(1);

    UserEntity entity = new UserEntity();
    entity.setUserId(userId);
    entity.setUserRol(role);
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
  @DisplayName("Test converter toDomain Ok")
  void toDomainOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    UserEntity userEntityInput = crearUserEntity(1, "admin", fechaFija);

    // ESPERADO
    User userEsperado = crearUser(1, "admin", fechaFija);

    // EJECUCIÓN
    User res = userOutConverter.toDomain(userEntityInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(userEsperado.getUserId(), res.getUserId());
          assertEquals(userEsperado.getRolesRoleId(), res.getRolesRoleId());
          assertEquals(userEsperado.getUsername(), res.getUsername());
          assertEquals(userEsperado.getEmail(), res.getEmail());
          assertEquals(userEsperado.getPassword(), res.getPassword());
          assertEquals(userEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toDomain Null")
  void toDomainNullTest() {

    // INPUT
    UserEntity userEntityInput = null;

    // EJECUCIÓN
    User res = userOutConverter.toDomain(userEntityInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter toEntity Ok")
  void toEntityOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    User userInput = crearUser(1, "admin", fechaFija);

    // ESPERADO
    UserEntity userEntityEsperado = crearUserEntity(1, "admin", fechaFija);
    userEntityEsperado.setUserRol(null);

    // EJECUCIÓN
    UserEntity res = userOutConverter.toEntity(userInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(userEntityEsperado.getUserId(), res.getUserId());
          assertEquals(userEntityEsperado.getUsername(), res.getUsername());
          assertEquals(userEntityEsperado.getEmail(), res.getEmail());
          assertEquals(userEntityEsperado.getPassword(), res.getPassword());
          assertEquals(userEntityEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toEntity Null")
  void toEntityNullTest() {

    // INPUT
    User userInput = null;

    // EJECUCIÓN
    UserEntity res = userOutConverter.toEntity(userInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter toPageable Unsorted Ok")
  void toPageableUnsortedOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10);

    // EJECUCIÓN
    Pageable res = userOutConverter.toPageable(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageableEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toPageable Sorted ASC Ok")
  void toPageableSortedAscOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "username: ASC");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by("username").ascending());

    // EJECUCIÓN
    Pageable res = userOutConverter.toPageable(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageableEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toPageable Sorted DESC Ok")
  void toPageableSortedDescOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "username: DESC");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by("username").descending());

    // EJECUCIÓN
    Pageable res = userOutConverter.toPageable(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageableEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toPageable Ko")
  void toPageableKoTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "FORMATO_INVALIDO");

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(
        ArrayIndexOutOfBoundsException.class,
        () -> userOutConverter.toPageable(pageableModelInput));
  }

  @Test
  @DisplayName("Test converter toUserPageModel Ok")
  void toUserPageModelOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    Pageable pageable = PageRequest.of(0, 10);
    Page<UserEntity> pageInput =
        new PageImpl<>(
            List.of(
                crearUserEntity(1, "admin", fechaFija),
                crearUserEntity(2, "juan.perez", fechaFija)),
            pageable,
            2);

    // ESPERADO
    PageModel<User> pageModelEsperado =
        new PageModel<>(
            List.of(crearUser(1, "admin", fechaFija), crearUser(2, "juan.perez", fechaFija)),
            2,
            1,
            2,
            10,
            0);

    // EJECUCIÓN
    PageModel<User> res = userOutConverter.toUserPageModel(pageInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toUserPageModel Ko")
  void toUserPageModelKoTest() {

    // INPUT
    Page<UserEntity> pageInput = null;

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(NullPointerException.class, () -> userOutConverter.toUserPageModel(pageInput));
  }
}
