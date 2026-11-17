package org.vedruna.palcole.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;

@ExtendWith(MockitoExtension.class)
class SelectRoleByIdUseCaseImplTest {

  @Mock private RoleStorage roleStorage;

  @InjectMocks private SelectRoleByIdUseCaseImpl selectRoleByIdUseCaseImpl;

  @Test
  void selectRoleById_ReturnsRole() {
    Role role = new Role();
    role.setRoleId(1);
    role.setName("ADMIN");

    when(roleStorage.findRoleById(1)).thenReturn(role);

    Role result = selectRoleByIdUseCaseImpl.selectRoleById(1);

    assertNotNull(result);
    assertEquals(1, result.getRoleId());
    assertEquals("ADMIN", result.getName());
    verify(roleStorage).findRoleById(1);
  }
}
