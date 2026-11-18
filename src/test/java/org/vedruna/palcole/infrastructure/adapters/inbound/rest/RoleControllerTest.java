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
import org.vedruna.palcole.domain.exception.RoleAlreadyExistsException;
import org.vedruna.palcole.domain.exception.RoleNotFoundException;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.DeleteRoleUseCase;
import org.vedruna.palcole.domain.ports.inbound.NewRoleUseCase;
import org.vedruna.palcole.domain.ports.inbound.SelectAllRolesUseCase;
import org.vedruna.palcole.domain.ports.inbound.SelectRoleByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.UpdateRoleUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.RoleInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.InsertRoleDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.RoleDTO;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

  @Mock private NewRoleUseCase newRoleUseCase;

  @Mock private DeleteRoleUseCase deleteRoleUseCase;

  @Mock private SelectAllRolesUseCase selectAllRolesUseCase;

  @Mock private SelectRoleByIdUseCase selectRoleByIdUseCase;

  @Mock private UpdateRoleUseCase updateRoleUseCase;

  @Mock private RoleInConverter roleInConverter;

  @InjectMocks private RoleController roleController;

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

  private InsertRoleDTO crearInsertRoleDTO(String name) {
    InsertRoleDTO dto = new InsertRoleDTO();
    dto.setName(name);
    return dto;
  }

  @Test
  @DisplayName("Test controlador getAll Ok")
  void getAllOkTest() {

    // INPUT
    Pageable pageableInput = PageRequest.of(0, 10);

    // ESPERADO
    List<RoleDTO> lista = List.of(crearRoleDTO(1, "ADMIN"), crearRoleDTO(2, "USER"));

    Pageable pageableEsperado = PageRequest.of(0, 10);

    Page<RoleDTO> pageEsperado = new PageImpl<>(lista, pageableEsperado, 2);

    ResponseEntity<Page<RoleDTO>> resEsperado = ResponseEntity.ok(pageEsperado);

    // MOCKS
    // CONVERTER MOCK (Pageable -> PageableModel)
    PageableModel pageableModelMock = new PageableModel(0, 10, "UNSORTED");

    when(roleInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);

    // USE CASE MOCK
    PageModel<Role> pageModelMock =
        new PageModel<>(List.of(crearRole(1, "ADMIN"), crearRole(2, "USER")), 2, 1, 2, 10, 0);

    when(selectAllRolesUseCase.selectAllRoles(pageableModelMock)).thenReturn(pageModelMock);

    // CONVERTER MOCK (PageModel -> Page de DTOs)
    Page<RoleDTO> pageDtoMock =
        new PageImpl<>(
            List.of(crearRoleDTO(1, "ADMIN"), crearRoleDTO(2, "USER")), PageRequest.of(0, 10), 2);

    when(roleInConverter.toRolesPage(pageModelMock, pageableModelMock)).thenReturn(pageDtoMock);

    // EJECUCIÓN
    ResponseEntity<Page<RoleDTO>> res = roleController.getAllRoles(pageableInput);

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

    when(roleInConverter.toPageableModel(pageableInput)).thenReturn(pageableModelMock);
    when(selectAllRolesUseCase.selectAllRoles(pageableModelMock))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class, () -> roleController.getAllRoles(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
    verify(roleInConverter, never()).toRolesPage(any(), any());
  }

  @Test
  @DisplayName("Test controlador insertIntoRole Ok")
  void insertIntoRoleOkTest() {

    // INPUT
    InsertRoleDTO insertRoleDTOInput = crearInsertRoleDTO("MODERATOR");

    // ESPERADO
    RoleDTO dtoEsperado = crearRoleDTO(3, "MODERATOR");

    ResponseEntity<RoleDTO> resEsperado =
        ResponseEntity.status(HttpStatus.CREATED).body(dtoEsperado);

    // MOCKS
    Role roleNuevoMock = crearRole(null, "MODERATOR");
    Role roleCreadoMock = crearRole(3, "MODERATOR");

    when(roleInConverter.toDomainInsert(insertRoleDTOInput)).thenReturn(roleNuevoMock);
    when(newRoleUseCase.newRole(roleNuevoMock)).thenReturn(roleCreadoMock);
    when(roleInConverter.toDto(roleCreadoMock)).thenReturn(crearRoleDTO(3, "MODERATOR"));

    // EJECUCIÓN
    ResponseEntity<RoleDTO> res = roleController.insertIntoRole(insertRoleDTOInput);

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
  @DisplayName("Test controlador insertIntoRole Ko")
  void insertIntoRoleKoTest() {

    // INPUT
    InsertRoleDTO insertRoleDTOInput = crearInsertRoleDTO("ADMIN");

    // MOCKS
    Role roleNuevoMock = crearRole(null, "ADMIN");

    when(roleInConverter.toDomainInsert(insertRoleDTOInput)).thenReturn(roleNuevoMock);
    when(newRoleUseCase.newRole(roleNuevoMock)).thenThrow(new RoleAlreadyExistsException("ADMIN"));

    // EJECUCIÓN Y VALIDACIONES
    RoleAlreadyExistsException ex =
        assertThrows(
            RoleAlreadyExistsException.class,
            () -> roleController.insertIntoRole(insertRoleDTOInput));

    assertEquals("Ya existe un role con el nombre: ADMIN", ex.getMessage());
    verify(roleInConverter, never()).toDto(any(Role.class));
  }

  @Test
  @DisplayName("Test controlador getRoleById Ok")
  void getRoleByIdOkTest() {

    // INPUT
    Integer roleIdInput = 1;

    // ESPERADO
    RoleDTO dtoEsperado = crearRoleDTO(1, "ADMIN");

    ResponseEntity<RoleDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    Role roleMock = crearRole(1, "ADMIN");

    when(selectRoleByIdUseCase.selectRoleById(roleIdInput)).thenReturn(roleMock);
    when(roleInConverter.toDto(roleMock)).thenReturn(crearRoleDTO(1, "ADMIN"));

    // EJECUCIÓN
    ResponseEntity<RoleDTO> res = roleController.getRoleById(roleIdInput);

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
  @DisplayName("Test controlador getRoleById Ko")
  void getRoleByIdKoTest() {

    // INPUT
    Integer roleIdInput = 99;

    // MOCKS
    when(selectRoleByIdUseCase.selectRoleById(roleIdInput))
        .thenThrow(new RoleNotFoundException(roleIdInput));

    // EJECUCIÓN Y VALIDACIONES
    RoleNotFoundException ex =
        assertThrows(RoleNotFoundException.class, () -> roleController.getRoleById(roleIdInput));

    assertEquals("No se encuentra el role con id: 99", ex.getMessage());
    verify(roleInConverter, never()).toDto(any(Role.class));
  }

  @Test
  @DisplayName("Test controlador updateRole Ok")
  void updateRoleOkTest() {

    // INPUT
    Integer roleIdInput = 1;
    InsertRoleDTO insertRoleDTOInput = crearInsertRoleDTO("TEACHER");

    // ESPERADO
    RoleDTO dtoEsperado = crearRoleDTO(1, "TEACHER");

    ResponseEntity<RoleDTO> resEsperado = ResponseEntity.ok(dtoEsperado);

    // MOCKS
    Role roleNuevosDatosMock = crearRole(null, "TEACHER");
    Role roleActualizadoMock = crearRole(1, "TEACHER");

    when(roleInConverter.toDomainInsert(insertRoleDTOInput)).thenReturn(roleNuevosDatosMock);
    when(updateRoleUseCase.updateRole(roleIdInput, roleNuevosDatosMock))
        .thenReturn(roleActualizadoMock);
    when(roleInConverter.toDto(roleActualizadoMock)).thenReturn(crearRoleDTO(1, "TEACHER"));

    // EJECUCIÓN
    ResponseEntity<RoleDTO> res = roleController.updateRole(roleIdInput, insertRoleDTOInput);

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
  @DisplayName("Test controlador updateRole Ko")
  void updateRoleKoTest() {

    // INPUT
    Integer roleIdInput = 99;
    InsertRoleDTO insertRoleDTOInput = crearInsertRoleDTO("TEACHER");

    // MOCKS
    Role roleNuevosDatosMock = crearRole(null, "TEACHER");

    when(roleInConverter.toDomainInsert(insertRoleDTOInput)).thenReturn(roleNuevosDatosMock);
    when(updateRoleUseCase.updateRole(roleIdInput, roleNuevosDatosMock))
        .thenThrow(new RoleNotFoundException(roleIdInput));

    // EJECUCIÓN Y VALIDACIONES
    RoleNotFoundException ex =
        assertThrows(
            RoleNotFoundException.class,
            () -> roleController.updateRole(roleIdInput, insertRoleDTOInput));

    assertEquals("No se encuentra el role con id: 99", ex.getMessage());
    verify(roleInConverter, never()).toDto(any(Role.class));
  }

  @Test
  @DisplayName("Test controlador deleteRole Ok")
  void deleteRoleOkTest() {

    // INPUT
    Integer roleIdInput = 1;

    // ESPERADO
    ResponseEntity<Void> resEsperado = ResponseEntity.noContent().build();

    // MOCKS
    doNothing().when(deleteRoleUseCase).deleteRole(roleIdInput);

    // EJECUCIÓN
    ResponseEntity<Void> res = roleController.deleteRole(roleIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
          assertNull(res.getBody());
          assertEquals(resEsperado, res);
        });
    verify(deleteRoleUseCase).deleteRole(roleIdInput);
  }

  @Test
  @DisplayName("Test controlador deleteRole Ko")
  void deleteRoleKoTest() {

    // INPUT
    Integer roleIdInput = 99;

    // MOCKS
    doThrow(new RoleNotFoundException(roleIdInput)).when(deleteRoleUseCase).deleteRole(roleIdInput);

    // EJECUCIÓN Y VALIDACIONES
    RoleNotFoundException ex =
        assertThrows(RoleNotFoundException.class, () -> roleController.deleteRole(roleIdInput));

    assertEquals("No se encuentra el role con id: 99", ex.getMessage());
  }
}
