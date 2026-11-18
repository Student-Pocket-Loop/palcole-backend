package org.vedruna.palcole.infrastructure.adapters.inbound.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.InsertRoleDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.RoleDTO;

class RoleInConverterTest {

  private final RoleInConverter converter = Mappers.getMapper(RoleInConverter.class);

  private Role crearRole(Integer roleId, String name) {
    Role role = new Role();
    role.setRoleId(roleId);
    role.setName(name);
    return role;
  }

  private RoleDTO crearRoleDTO(Integer roleId, String name) {
    RoleDTO dto = new RoleDTO();
    dto.setRoleId(roleId);
    dto.setName(name);
    return dto;
  }

  @Test
  @DisplayName("Test converter inbound toDomain Ok")
  void toDomainOkTest() {

    // INPUT
    RoleDTO dtoInput = crearRoleDTO(1, "ADMIN");

    // EJECUCIÓN
    Role res = converter.toDomain(dtoInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(1, res.getRoleId());
          assertEquals("ADMIN", res.getName());
        });
  }

  @Test
  @DisplayName("Test converter inbound toDomainInsert Ok")
  void toDomainInsertOkTest() {

    // INPUT
    InsertRoleDTO insertDtoInput = new InsertRoleDTO();
    insertDtoInput.setName("MODERATOR");

    // EJECUCIÓN
    Role res = converter.toDomainInsert(insertDtoInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertNull(res.getRoleId());
          assertEquals("MODERATOR", res.getName());
        });
  }

  @Test
  @DisplayName("Test converter inbound toDto Ok")
  void toDtoOkTest() {

    // INPUT
    Role roleInput = crearRole(2, "USER");

    // EJECUCIÓN
    RoleDTO res = converter.toDto(roleInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getRoleId());
          assertEquals("USER", res.getName());
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
    Pageable pageableInput = PageRequest.of(0, 10, Sort.by("name").ascending());

    // EJECUCIÓN
    PageableModel res = converter.toPageableModel(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertEquals(0, res.getPage());
          assertEquals(10, res.getSize());
          assertEquals("name: ASC", res.getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toRolesPage Ok (sin ordenar)")
  void toRolesPageUnsortedOkTest() {

    // INPUT
    PageModel<Role> pageModelInput =
        new PageModel<>(List.of(crearRole(1, "ADMIN"), crearRole(2, "USER")), 2, 1, 2, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    Page<RoleDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearRoleDTO(1, "ADMIN"), crearRoleDTO(2, "USER")), PageRequest.of(0, 10), 2);

    // EJECUCIÓN
    Page<RoleDTO> res = converter.toRolesPage(pageModelInput, pageableModelInput);

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
  @DisplayName("Test converter inbound toRolesPage Ok (orden ascendente)")
  void toRolesPageAscOkTest() {

    // INPUT
    PageModel<Role> pageModelInput =
        new PageModel<>(List.of(crearRole(1, "ADMIN")), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "name: ASC");

    // ESPERADO
    Page<RoleDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearRoleDTO(1, "ADMIN")),
            PageRequest.of(0, 10, Sort.by("name").ascending()),
            1);

    // EJECUCIÓN
    Page<RoleDTO> res = converter.toRolesPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("name").ascending(), res.getPageable().getSort());
        });
  }

  @Test
  @DisplayName("Test converter inbound toRolesPage Ok (orden descendente)")
  void toRolesPageDescOkTest() {

    // INPUT
    PageModel<Role> pageModelInput =
        new PageModel<>(List.of(crearRole(1, "ADMIN")), 1, 1, 1, 10, 0);
    PageableModel pageableModelInput = new PageableModel(0, 10, "name: DESC");

    // ESPERADO
    Page<RoleDTO> pageEsperado =
        new PageImpl<>(
            List.of(crearRoleDTO(1, "ADMIN")),
            PageRequest.of(0, 10, Sort.by("name").descending()),
            1);

    // EJECUCIÓN
    Page<RoleDTO> res = converter.toRolesPage(pageModelInput, pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(pageEsperado, res);
          assertEquals(Sort.by("name").descending(), res.getPageable().getSort());
        });
  }
}
