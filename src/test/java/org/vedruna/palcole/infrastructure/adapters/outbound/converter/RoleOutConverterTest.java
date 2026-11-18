package org.vedruna.palcole.infrastructure.adapters.outbound.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.persistance.model.RoleEntity;

class RoleOutConverterTest {

  private final RoleOutConverter roleOutConverter = Mappers.getMapper(RoleOutConverter.class);

  private Role crearRole(Integer roleId, String name) {
    Role role = new Role();
    role.setRoleId(roleId);
    role.setName(name);
    return role;
  }

  private RoleEntity crearRoleEntity(Integer userId, String name) {
    RoleEntity entity = new RoleEntity();
    entity.setUserId(userId);
    entity.setName(name);
    return entity;
  }

  @Test
  @DisplayName("Test converter toDomain Ok")
  void toDomainOkTest() {

    // INPUT
    RoleEntity roleEntityInput = crearRoleEntity(1, "ADMIN");

    // ESPERADO
    Role roleEsperado = crearRole(1, "ADMIN");

    // EJECUCIÓN
    Role res = roleOutConverter.toDomain(roleEntityInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(roleEsperado.getRoleId(), res.getRoleId());
          assertEquals(roleEsperado.getName(), res.getName());
          assertEquals(roleEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toDomain Null")
  void toDomainNullTest() {

    // INPUT
    RoleEntity roleEntityInput = null;

    // EJECUCIÓN
    Role res = roleOutConverter.toDomain(roleEntityInput);

    // VALIDACIONES
    assertNull(res);
  }

  @Test
  @DisplayName("Test converter toEntity Ok")
  void toEntityOkTest() {

    // INPUT
    Role roleInput = crearRole(1, "ADMIN");

    // ESPERADO
    RoleEntity roleEntityEsperado = crearRoleEntity(1, "ADMIN");

    // EJECUCIÓN
    RoleEntity res = roleOutConverter.toEntity(roleInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(roleEntityEsperado.getUserId(), res.getUserId());
          assertEquals(roleEntityEsperado.getName(), res.getName());
          assertEquals(roleEntityEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toEntity Null")
  void toEntityNullTest() {

    // INPUT
    Role roleInput = null;

    // EJECUCIÓN
    RoleEntity res = roleOutConverter.toEntity(roleInput);

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
    Pageable res = roleOutConverter.toPageable(pageableModelInput);

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
    PageableModel pageableModelInput = new PageableModel(0, 10, "name: ASC");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by("name").ascending());

    // EJECUCIÓN
    Pageable res = roleOutConverter.toPageable(pageableModelInput);

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
    PageableModel pageableModelInput = new PageableModel(0, 10, "name: DESC");

    // ESPERADO
    Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by("name").descending());

    // EJECUCIÓN
    Pageable res = roleOutConverter.toPageable(pageableModelInput);

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
        () -> roleOutConverter.toPageable(pageableModelInput));
  }

  @Test
  @DisplayName("Test converter toRolePageModel Ok")
  void toRolePageModelOkTest() {

    // INPUT
    Pageable pageable = PageRequest.of(0, 10);
    Page<RoleEntity> pageInput =
        new PageImpl<>(
            List.of(crearRoleEntity(1, "ADMIN"), crearRoleEntity(2, "USER")), pageable, 2);

    // ESPERADO
    PageModel<Role> pageModelEsperado =
        new PageModel<>(List.of(crearRole(1, "ADMIN"), crearRole(2, "USER")), 2, 1, 2, 10, 0);

    // EJECUCIÓN
    PageModel<Role> res = roleOutConverter.toRolePageModel(pageInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test converter toRolePageModel Ko")
  void toRolePageModelKoTest() {

    // INPUT
    Page<RoleEntity> pageInput = null;

    // EJECUCIÓN Y VALIDACIONES
    assertThrows(NullPointerException.class, () -> roleOutConverter.toRolePageModel(pageInput));
  }
}
