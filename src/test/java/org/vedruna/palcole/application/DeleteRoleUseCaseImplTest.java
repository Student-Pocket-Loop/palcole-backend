package org.vedruna.palcole.application;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;

@ExtendWith(MockitoExtension.class)
class DeleteRoleUseCaseImplTest {

  @Mock private RoleStorage roleStorage;

  @InjectMocks private DeleteRoleUseCaseImpl deleteRoleUseCaseImpl;

  @Test
  void deleteRole_CallsStorageDeleteRole() {
    doNothing().when(roleStorage).deleteRole(1);

    deleteRoleUseCaseImpl.deleteRole(1);

    verify(roleStorage).deleteRole(1);
  }
}
