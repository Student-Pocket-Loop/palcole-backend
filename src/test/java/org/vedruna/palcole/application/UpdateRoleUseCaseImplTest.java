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
class UpdateRoleUseCaseImplTest {

  @Mock private RoleStorage roleStorage;

  @InjectMocks private UpdateRoleUseCaseImpl updateRoleUseCaseImpl;

  @Test
  void updateRole_ReturnsUpdatedRole() {
    Role newRoleData = new Role();
    newRoleData.setName("TEACHER");

    Role updatedRole = new Role();
    updatedRole.setRoleId(1);
    updatedRole.setName("TEACHER");

    when(roleStorage.updateRole(1, newRoleData)).thenReturn(updatedRole);

    Role result = updateRoleUseCaseImpl.updateRole(1, newRoleData);

    assertNotNull(result);
    assertEquals(1, result.getRoleId());
    assertEquals("TEACHER", result.getName());
    verify(roleStorage).updateRole(1, newRoleData);
  }
}
