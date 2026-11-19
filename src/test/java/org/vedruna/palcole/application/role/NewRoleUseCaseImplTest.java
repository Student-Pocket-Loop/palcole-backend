package org.vedruna.palcole.application.role;

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
import org.vedruna.palcole.domain.exception.roles.RoleAlreadyExistsException;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;

@ExtendWith(MockitoExtension.class)
class NewRoleUseCaseImplTest {

  @Mock private RoleStorage roleStorage;

  @InjectMocks private NewRoleUseCaseImpl newRoleUseCaseImpl;

  private Role crearRole(Integer roleId, String name) {
    Role role = new Role();
    role.setRoleId(roleId);
    role.setName(name);
    return role;
  }

  @Test
  @DisplayName("Test caso de uso newRole Ok")
  void newRoleOkTest() {

    // INPUT
    Role newRoleInput = crearRole(null, "MODERATOR");

    // ESPERADO
    Role roleEsperado = crearRole(3, "MODERATOR");

    // MOCKS
    Role roleCreadoMock = crearRole(3, "MODERATOR");

    when(roleStorage.insertRole(newRoleInput)).thenReturn(roleCreadoMock);

    // EJECUCIÓN
    Role res = newRoleUseCaseImpl.newRole(newRoleInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(roleEsperado, res);
        });
    verify(roleStorage).insertRole(newRoleInput);
  }

  @Test
  @DisplayName("Test caso de uso newRole Ko")
  void newRoleKoTest() {

    // INPUT
    Role newRoleInput = crearRole(null, "ADMIN");

    // MOCKS
    when(roleStorage.insertRole(newRoleInput)).thenThrow(new RoleAlreadyExistsException("ADMIN"));

    // EJECUCIÓN Y VALIDACIONES
    RoleAlreadyExistsException ex =
        assertThrows(
            RoleAlreadyExistsException.class, () -> newRoleUseCaseImpl.newRole(newRoleInput));

    assertEquals("Ya existe un role con el nombre: ADMIN", ex.getMessage());
    verify(roleStorage).insertRole(newRoleInput);
  }
}
