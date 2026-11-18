package org.vedruna.palcole.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.exception.RoleNotFoundException;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;

@ExtendWith(MockitoExtension.class)
class DeleteRoleUseCaseImplTest {

  @Mock private RoleStorage roleStorage;

  @InjectMocks private DeleteRoleUseCaseImpl deleteRoleUseCaseImpl;

  @Test
  @DisplayName("Test caso de uso deleteRole Ok")
  void deleteRoleOkTest() {

    // INPUT
    Integer roleIdInput = 1;

    // MOCKS
    doNothing().when(roleStorage).deleteRole(roleIdInput);

    // EJECUCIÓN Y VALIDACIONES
    assertDoesNotThrow(() -> deleteRoleUseCaseImpl.deleteRole(roleIdInput));
    verify(roleStorage).deleteRole(roleIdInput);
  }

  @Test
  @DisplayName("Test caso de uso deleteRole Ko")
  void deleteRoleKoTest() {

    // INPUT
    Integer roleIdInput = 99;

    // MOCKS
    doThrow(new RoleNotFoundException(roleIdInput)).when(roleStorage).deleteRole(roleIdInput);

    // EJECUCIÓN Y VALIDACIONES
    RoleNotFoundException ex =
        assertThrows(
            RoleNotFoundException.class, () -> deleteRoleUseCaseImpl.deleteRole(roleIdInput));

    assertEquals("No se encuentra el role con id: 99", ex.getMessage());
  }
}
