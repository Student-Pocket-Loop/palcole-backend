package org.vedruna.palcole.infrastructure.adapters.inbound.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.user.InsertUserDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.user.UserDTO;

class UserInConverterTest {

  private final UserInConverter converter = Mappers.getMapper(UserInConverter.class);

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

  @Test
  @DisplayName("Test converter inbound toDomain Ok")
  void toDomainOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    UserDTO dtoInput = crearUserDTO(1, "admin", fechaFija);

    // EJECUCIÓN
    User res = converter.toDomain(dtoInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(1, res.getUserId());
          assertEquals(1, res.getRolesRoleId());
          assertEquals("admin", res.getUsername());
          assertEquals("admin@vedruna.org", res.getEmail());
        });
  }

  @Test
  @DisplayName("Test converter inbound toDomainInsert Ok")
  void toDomainInsertOkTest() {

    // INPUT
    InsertUserDTO insertDtoInput = new InsertUserDTO();
    insertDtoInput.setUsername("juan.perez");
    insertDtoInput.setEmail("juan.perez@vedruna.org");
    insertDtoInput.setPassword("password123");

    // EJECUCIÓN
    User res = converter.toDomainInsert(insertDtoInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertNull(res.getUserId());
          assertEquals(1, res.getRolesRoleId());
          assertEquals("juan.perez", res.getUsername());
          assertEquals("juan.perez@vedruna.org", res.getEmail());
        });
  }

  @Test
  @DisplayName("Test converter inbound toDto Ok")
  void toDtoOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    User userInput = crearUser(2, "juan.perez", fechaFija);

    // EJECUCIÓN
    UserDTO res = converter.toDto(userInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getUserId());
          assertEquals(1, res.getRolesRoleId());
          assertEquals("juan.perez", res.getUsername());
          assertEquals("juan.perez@vedruna.org", res.getEmail());
        });
  }

  @Test
  @DisplayName("Test converter inbound toPageableModel Ok (sin ordenar)")
  void toPageableModelUnsortedOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(1, 5);

    // EJECUCIÓN
    PageableModel res = converter.toPageableModel(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertEquals(1, res.getPage());
          assertEquals(5, res.getSize());
          assertEquals("UNSORTED", res.getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toPageableModel Ok (ordenado)")
  void toPageableModelSortedOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10, Sort.by("username").ascending());

    // EJECUCIÓN
    PageableModel res = converter.toPageableModel(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertEquals(0, res.getPage());
          assertEquals(10, res.getSize());
          assertEquals("username: ASC", res.getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toUsersPage Ok (sin ordenar)")
  void toUsersPageUnsortedOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    PageModel<User> pageModelInput =
        new PageModel<>(
            List.of(crearUser(1, "admin", fechaFija), crearUser(2, "juan.perez", fechaFija)),
            2,
            1,
            2,
            10,
            0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    Page<UserDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearUserDTO(1, "admin", fechaFija), crearUserDTO(2, "juan.perez", fechaFija)),
            PageRequest.of(0, 10),
            2);

    // EJECUCIÓN
    Page<UserDTO> res = converter.toUsersPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(2, res.getTotalElements());
          assertEquals(pageEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter inbound toUsersPage Ok (orden ascendente)")
  void toUsersPageAscOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    PageModel<User> pageModelInput =
        new PageModel<>(List.of(crearUser(1, "admin", fechaFija)), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "username: ASC");

    // ESPERADO
    Page<UserDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearUserDTO(1, "admin", fechaFija)),
            PageRequest.of(0, 10, Sort.by("username").ascending()),
            1);

    // EJECUCIÓN
    Page<UserDTO> res = converter.toUsersPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("username").ascending(), res.getPageable().getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toUsersPage Ok (orden descendente)")
  void toUsersPageDescOkTest() {

    LocalDateTime fechaFija = LocalDateTime.of(2026, 1, 1, 10, 0);

    // INPUT
    PageModel<User> pageModelInput =
        new PageModel<>(List.of(crearUser(1, "admin", fechaFija)), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "username: DESC");

    // ESPERADO
    Page<UserDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearUserDTO(1, "admin", fechaFija)),
            PageRequest.of(0, 10, Sort.by("username").descending()),
            1);

    // EJECUCIÓN
    Page<UserDTO> res = converter.toUsersPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("username").descending(), res.getPageable().getSort());
        });
  }
}
