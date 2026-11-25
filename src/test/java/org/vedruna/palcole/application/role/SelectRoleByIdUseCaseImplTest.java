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
import org.vedruna.palcole.domain.exception.roles.RoleNotFoundException;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;

@ExtendWith(MockitoExtension.class)
class SelectRoleByIdUseCaseImplTest {

  @Mock private RoleStorage roleStorage;

  @InjectMocks private SelectRoleByIdUseCaseImpl selectRoleByIdUseCaseImpl;

  @Test
  @DisplayName("Test caso de uso selectRoleById Ok")
  void selectRoleByIdOkTest() {

    // INPUT
    Integer roleIdInput = 1;

    // ESPERADO
    Role roleEsperado = new Role();
    roleEsperado.setRoleId(1);
    roleEsperado.setName("ADMIN");

    // MOCKS
    when(roleStorage.findRoleById(roleIdInput)).thenReturn(roleEsperado);

    // EJECUCIÓN
    Role res = selectRoleByIdUseCaseImpl.selectRoleById(roleIdInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(1, res.getRoleId());
          assertEquals("ADMIN", res.getName());
        });
    verify(roleStorage).findRoleById(roleIdInput);
  }

  @Test
  @DisplayName("Test caso de uso selectRoleById Ko")
  void selectRoleByIdKoTest() {

    // INPUT
    Integer roleIdInput = 99;

    // MOCKS
    when(roleStorage.findRoleById(roleIdInput)).thenThrow(new RoleNotFoundException(roleIdInput));

    // EJECUCIÓN Y VALIDACIONES
    RoleNotFoundException ex =
        assertThrows(
            RoleNotFoundException.class,
            () -> selectRoleByIdUseCaseImpl.selectRoleById(roleIdInput));

    assertEquals("No se encuentra el role con id: 99", ex.getMessage());
  }
}
