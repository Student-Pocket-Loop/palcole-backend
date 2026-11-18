package org.vedruna.palcole.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.exception.RoleNotFoundException;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;

@ExtendWith(MockitoExtension.class)
class UpdateRoleUseCaseImplTest {

  @Mock private RoleStorage roleStorage;

  @InjectMocks private UpdateRoleUseCaseImpl updateRoleUseCaseImpl;

  private Role crearRole(Integer roleId, String name) {
    Role role = new Role();
    role.setRoleId(roleId);
    role.setName(name);
    return role;
  }

  @Test
  @DisplayName("Test caso de uso updateRole Ok")
  void updateRoleOkTest() {

    // INPUT
    Integer roleIdInput = 1;
    Role newRoleInput = crearRole(null, "TEACHER");

    // ESPERADO
    Role roleEsperado = crearRole(1, "TEACHER");

    // MOCKS
    Role roleActualizadoMock = crearRole(1, "TEACHER");

    when(roleStorage.updateRole(roleIdInput, newRoleInput)).thenReturn(roleActualizadoMock);

    // EJECUCIÓN
    Role res = updateRoleUseCaseImpl.updateRole(roleIdInput, newRoleInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(roleEsperado, res);
        });
    verify(roleStorage).updateRole(roleIdInput, newRoleInput);
  }

  @Test
  @DisplayName("Test caso de uso updateRole Ko")
  void updateRoleKoTest() {

    // INPUT
    Integer roleIdInput = 99;
    Role newRoleInput = crearRole(null, "TEACHER");

    // MOCKS
    when(roleStorage.updateRole(roleIdInput, newRoleInput))
        .thenThrow(new RoleNotFoundException(roleIdInput));

    // EJECUCIÓN Y VALIDACIONES
    RoleNotFoundException ex =
        assertThrows(
            RoleNotFoundException.class,
            () -> updateRoleUseCaseImpl.updateRole(roleIdInput, newRoleInput));

    assertEquals("No se encuentra el role con id: 99", ex.getMessage());
    verify(roleStorage).updateRole(roleIdInput, newRoleInput);
  }
}
