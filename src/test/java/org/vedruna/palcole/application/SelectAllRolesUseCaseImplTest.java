package org.vedruna.palcole.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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

  @Test
  void selectAllRoles_ReturnsPageModelOfRoles() {
    PageableModel pageableModel = new PageableModel();
    Role role = new Role();
    role.setRoleId(1);
    role.setName("ADMIN");

    PageModel<Role> expectedPage = new PageModel<>();
    expectedPage.setContent(List.of(role));

    when(roleStorage.selectAllRoles(pageableModel)).thenReturn(expectedPage);

    PageModel<Role> result = selectAllRolesUseCaseImpl.selectAllRoles(pageableModel);

    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals("ADMIN", result.getContent().get(0).getName());
    verify(roleStorage).selectAllRoles(pageableModel);
  }
}
