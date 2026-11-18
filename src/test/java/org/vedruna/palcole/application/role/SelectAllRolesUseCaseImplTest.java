package org.vedruna.palcole.application.role;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;

@ExtendWith(MockitoExtension.class)
class SelectAllRolesUseCaseImplTest {

  @Mock private RoleStorage roleStorage;

  @InjectMocks private SelectAllRolesUseCaseImpl selectAllRolesUseCaseImpl;

  private Role crearRole(Integer roleId, String name) {
    Role role = new Role();
    role.setRoleId(roleId);
    role.setName(name);
    return role;
  }

  @Test
  @DisplayName("Test caso de uso selectAllRoles Ok")
  void selectAllRolesOkTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "UNSORTED");

    // ESPERADO
    PageModel<Role> pageEsperado =
        new PageModel<>(List.of(crearRole(1, "ADMIN"), crearRole(2, "USER")), 2, 1, 2, 10, 0);

    // MOCKS
    when(roleStorage.selectAllRoles(pageableInput)).thenReturn(pageEsperado);

    // EJECUCIÓN
    PageModel<Role> res = selectAllRolesUseCaseImpl.selectAllRoles(pageableInput);

    // VALIDACIONES
    assertAll(
        () -> {
          assertNotNull(res);
          assertEquals(2, res.getContent().size());
          assertEquals("ADMIN", res.getContent().get(0).getName());
          assertEquals("USER", res.getContent().get(1).getName());
          assertEquals(2, res.getTotalElements());
        });
    verify(roleStorage).selectAllRoles(pageableInput);
  }

  @Test
  @DisplayName("Test caso de uso selectAllRoles Ko")
  void selectAllRolesKoTest() {

    // INPUT
    PageableModel pageableInput = new PageableModel(0, 10, "inexistente: ASC");

    // MOCKS
    when(roleStorage.selectAllRoles(pageableInput))
        .thenThrow(new IllegalArgumentException("Propiedad de ordenación inválida"));

    // EJECUCIÓN Y VALIDACIONES
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> selectAllRolesUseCaseImpl.selectAllRoles(pageableInput));

    assertEquals("Propiedad de ordenación inválida", ex.getMessage());
  }
}
