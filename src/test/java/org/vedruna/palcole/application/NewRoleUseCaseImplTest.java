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
class NewRoleUseCaseImplTest {

  @Mock private RoleStorage roleStorage;

  @InjectMocks private NewRoleUseCaseImpl newRoleUseCaseImpl;

  @Test
  void newRole_CallsInsertRoleAndReturnsSavedRole() {
    Role inputRole = new Role();
    inputRole.setName("ADMIN");

    Role savedRole = new Role();
    savedRole.setRoleId(1);
    savedRole.setName("ADMIN");

    when(roleStorage.insertRole(inputRole)).thenReturn(savedRole);

    Role result = newRoleUseCaseImpl.newRole(inputRole);

    assertNotNull(result);
    assertEquals(1, result.getRoleId());
    assertEquals("ADMIN", result.getName());
    verify(roleStorage).insertRole(inputRole);
  }
}
