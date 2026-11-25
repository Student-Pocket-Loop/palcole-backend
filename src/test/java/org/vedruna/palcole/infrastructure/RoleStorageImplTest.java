package org.vedruna.palcole.infrastructure;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.vedruna.palcole.domain.exception.roles.RoleNotFoundException;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.RoleOutConverter;
import org.vedruna.palcole.persistance.model.RoleEntity;
import org.vedruna.palcole.persistance.repository.RoleRepository;

@ExtendWith(MockitoExtension.class)
class RoleStorageImplTest {

  @Mock private RoleRepository roleRepository;

  @Mock private RoleOutConverter roleOutConverter;

  @InjectMocks private RoleStorageImpl roleStorageImpl;

  private Role crearRole(Integer roleId, String name) {
    Role role = new Role();
    role.setRoleId(roleId);
    role.setName(name);
    return role;
  }

  private RoleEntity crearRoleEntity(Integer roleId, String name) {
    RoleEntity entity = new RoleEntity();
    entity.setUserId(roleId);
    entity.setName(name);
    return entity;
  }

  @Test
  @DisplayName("Test storage selectAllRoles Ok")
  void selectAllRolesOkTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<Role> pageModelEsperado =
        new PageModel<>(List.of(crearRole(1, "ADMIN"), crearRole(2, "USER")), 2, 1, 2, 10, 0);

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);
    Page<RoleEntity> pageEntityMock =
        new PageImpl<>(
            List.of(crearRoleEntity(1, "ADMIN"), crearRoleEntity(2, "USER")), pageableMock, 2);
    PageModel<Role> pageModelMock =
        new PageModel<>(List.of(crearRole(1, "ADMIN"), crearRole(2, "USER")), 2, 1, 2, 10, 0);

    when(roleOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(roleRepository.findAll(pageableMock)).thenReturn(pageEntityMock);
    when(roleOutConverter.toRolePageModel(pageEntityMock)).thenReturn(pageModelMock);

    // EJECUCIÓN
    PageModel<Role> res = roleStorageImpl.selectAllRoles(pageableModelInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals(pageModelEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage selectAllRoles Ko")
  void selectAllRolesKoTest() {

    // INPUT
    PageableModel pageableModelInput = new PageableModel(0, 10, "INVALID_SORT");

    // MOCKS
    Pageable pageableMock = PageRequest.of(0, 10);

    when(roleOutConverter.toPageable(pageableModelInput)).thenReturn(pageableMock);
    when(roleRepository.findAll(pageableMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> roleStorageImpl.selectAllRoles(pageableModelInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(roleOutConverter, never()).toRolePageModel(any());
  }

  @Test
  @DisplayName("Test storage findRoleById Ok")
  void findRoleByIdOkTest() {

    // INPUT
    Integer roleIdInput = 1;

    // ESPERADO
    Role roleEsperado = crearRole(1, "ADMIN");

    // MOCKS
    RoleEntity roleEntityMock = crearRoleEntity(1, "ADMIN");
    Role roleMock = crearRole(1, "ADMIN");

    when(roleRepository.findById(roleIdInput)).thenReturn(Optional.of(roleEntityMock));
    when(roleOutConverter.toDomain(roleEntityMock)).thenReturn(roleMock);

    // EJECUCIÓN
    Role res = roleStorageImpl.findRoleById(roleIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(roleEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage findRoleById Ko")
  void findRoleByIdKoTest() {

    // INPUT
    Integer roleIdInput = 99;

    // MOCKS
    when(roleRepository.findById(roleIdInput)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    RoleNotFoundException ex =
        assertThrows(RoleNotFoundException.class, () -> roleStorageImpl.findRoleById(roleIdInput));

    assertEquals("No se encuentra el role con id: 99", ex.getMessage());
    verify(roleOutConverter, never()).toDomain(any(RoleEntity.class));
  }

  @Test
  @DisplayName("Test storage updateRole Ok")
  void updateRoleOkTest() {

    // INPUT
    Integer roleIdInput = 1;
    Role newRoleInput = crearRole(null, "TEACHER");

    // ESPERADO
    Role roleEsperado = crearRole(1, "TEACHER");

    // MOCKS
    RoleEntity roleEntityExistenteMock = crearRoleEntity(1, "ADMIN");
    RoleEntity roleEntityGuardadoMock = crearRoleEntity(1, "TEACHER");
    Role roleActualizadoMock = crearRole(1, "TEACHER");

    when(roleRepository.findById(roleIdInput)).thenReturn(Optional.of(roleEntityExistenteMock));
    when(roleRepository.save(roleEntityExistenteMock)).thenReturn(roleEntityGuardadoMock);
    when(roleOutConverter.toDomain(roleEntityGuardadoMock)).thenReturn(roleActualizadoMock);

    // EJECUCIÓN
    Role res = roleStorageImpl.updateRole(roleIdInput, newRoleInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals("TEACHER", roleEntityExistenteMock.getName());
          assertEquals(roleEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage updateRole Ko")
  void updateRoleKoTest() {

    // INPUT
    Integer roleIdInput = 99;
    Role newRoleInput = crearRole(null, "TEACHER");

    // MOCKS
    when(roleRepository.findById(roleIdInput)).thenReturn(Optional.empty());

    // EJECUCIÓN Y VALIDACIONES
    RoleNotFoundException ex =
        assertThrows(
            RoleNotFoundException.class,
            () -> roleStorageImpl.updateRole(roleIdInput, newRoleInput));

    assertEquals("No se encuentra el role con id: 99", ex.getMessage());
    verify(roleRepository, never()).save(any(RoleEntity.class));
    verify(roleOutConverter, never()).toDomain(any(RoleEntity.class));
  }

  @Test
  @DisplayName("Test storage insertRole Ok")
  void insertRoleOkTest() {

    // INPUT
    Role newRoleInput = crearRole(null, "MODERATOR");

    // ESPERADO
    Role roleEsperado = crearRole(3, "MODERATOR");

    // MOCKS
    RoleEntity roleEntityNuevoMock = crearRoleEntity(null, "MODERATOR");
    RoleEntity roleEntityGuardadoMock = crearRoleEntity(3, "MODERATOR");
    Role roleCreadoMock = crearRole(3, "MODERATOR");

    when(roleOutConverter.toEntity(newRoleInput)).thenReturn(roleEntityNuevoMock);
    when(roleRepository.save(roleEntityNuevoMock)).thenReturn(roleEntityGuardadoMock);
    when(roleOutConverter.toDomain(roleEntityGuardadoMock)).thenReturn(roleCreadoMock);

    // EJECUCIÓN
    Role res = roleStorageImpl.insertRole(newRoleInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(roleEsperado, res);
        });
  }

  @Test
  @DisplayName("Test storage insertRole Ko")
  void insertRoleKoTest() {

    // INPUT
    Role newRoleInput = crearRole(null, "ADMIN");

    // MOCKS
    RoleEntity roleEntityNuevoMock = crearRoleEntity(null, "ADMIN");

    when(roleOutConverter.toEntity(newRoleInput)).thenReturn(roleEntityNuevoMock);
    when(roleRepository.save(roleEntityNuevoMock))
        .thenThrow(new DataIntegrityViolationException("El rol ya existe en base de datos"));

    // EJECUCIÓN Y VALIDACIONES
    DataIntegrityViolationException ex =
        assertThrows(
            DataIntegrityViolationException.class, () -> roleStorageImpl.insertRole(newRoleInput));

    assertEquals("El rol ya existe en base de datos", ex.getMessage());
    verify(roleOutConverter, never()).toDomain(any(RoleEntity.class));
  }

  @Test
  @DisplayName("Test storage deleteRole Ok")
  void deleteRoleOkTest() {

    // INPUT
    Integer roleIdInput = 1;

    // MOCKS
    when(roleRepository.existsById(roleIdInput)).thenReturn(true);
    doNothing().when(roleRepository).deleteById(roleIdInput);

    // EJECUCIÓN
    roleStorageImpl.deleteRole(roleIdInput);

    // VALIDACIONES
    verify(roleRepository).existsById(roleIdInput);
    verify(roleRepository).deleteById(roleIdInput);
  }

  @Test
  @DisplayName("Test storage deleteRole Ko")
  void deleteRoleKoTest() {

    // INPUT
    Integer roleIdInput = 99;

    // MOCKS
    when(roleRepository.existsById(roleIdInput)).thenReturn(false);

    // EJECUCIÓN Y VALIDACIONES
    RoleNotFoundException ex =
        assertThrows(RoleNotFoundException.class, () -> roleStorageImpl.deleteRole(roleIdInput));

    assertEquals("No se encuentra el role con id: 99", ex.getMessage());
    verify(roleRepository, never()).deleteById(any());
  }
}
