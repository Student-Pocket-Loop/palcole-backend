package org.vedruna.palcole.infrastructure.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.RoleStorageImpl;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.RoleOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.exception.RoleNotFoundException;
import org.vedruna.palcole.persistance.model.RoleEntity;
import org.vedruna.palcole.persistance.repository.RoleRepository;

@ExtendWith(MockitoExtension.class)
class RoleStorageImplTest {

  @Mock private RoleRepository roleRepository;
  @Mock private RoleOutConverter roleOutConverter;

  @InjectMocks private RoleStorageImpl roleStorageImpl;

  private RoleEntity roleEntity;
  private Role roleDomain;

  @BeforeEach
  void setUp() {
    roleEntity = new RoleEntity();
    roleEntity.setUserId(1);
    roleEntity.setName("ADMIN");

    roleDomain = new Role();
    roleDomain.setRoleId(1);
    roleDomain.setName("ADMIN");
  }

  @Test
  void selectAllRoles_ReturnsPageModel() {
    PageableModel pageableModel = new PageableModel();
    Pageable pageable = PageRequest.of(0, 10);
    Page<RoleEntity> entityPage = new PageImpl<>(List.of(roleEntity));
    PageModel<Role> expectedPageModel = new PageModel<>();
    expectedPageModel.setContent(List.of(roleDomain));

    when(roleOutConverter.toPageable(pageableModel)).thenReturn(pageable);
    when(roleRepository.findAll(pageable)).thenReturn(entityPage);
    when(roleOutConverter.toRolePageModel(entityPage)).thenReturn(expectedPageModel);

    PageModel<Role> result = roleStorageImpl.selectAllRoles(pageableModel);

    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals("ADMIN", result.getContent().get(0).getName());
  }

  @Test
  void findRoleById_WhenExists_ReturnsRole() {
    when(roleRepository.findById(1)).thenReturn(Optional.of(roleEntity));
    when(roleOutConverter.toDomain(roleEntity)).thenReturn(roleDomain);

    Role result = roleStorageImpl.findRoleById(1);

    assertEquals(1, result.getRoleId());
    assertEquals("ADMIN", result.getName());
  }

  @Test
  void findRoleById_WhenNotExists_ThrowsException() {
    when(roleRepository.findById(99)).thenReturn(Optional.empty());

    assertThrows(RoleNotFoundException.class, () -> roleStorageImpl.findRoleById(99));
  }

  @Test
  void insertRole_SavesAndReturnsRole() {
    when(roleOutConverter.toEntity(roleDomain)).thenReturn(roleEntity);
    when(roleRepository.save(roleEntity)).thenReturn(roleEntity);
    when(roleOutConverter.toDomain(roleEntity)).thenReturn(roleDomain);

    Role result = roleStorageImpl.insertRole(roleDomain);

    assertNotNull(result);
    assertEquals(1, result.getRoleId());
    verify(roleRepository).save(roleEntity);
  }

  @Test
  void updateRole_WhenExists_UpdatesAndReturnsRole() {
    Role newRoleData = new Role();
    newRoleData.setName("TEACHER");

    Role updatedDomain = new Role();
    updatedDomain.setRoleId(1);
    updatedDomain.setName("TEACHER");

    when(roleRepository.findById(1)).thenReturn(Optional.of(roleEntity));
    when(roleRepository.save(roleEntity)).thenReturn(roleEntity);
    when(roleOutConverter.toDomain(roleEntity)).thenReturn(updatedDomain);

    Role result = roleStorageImpl.updateRole(1, newRoleData);

    assertEquals(1, result.getRoleId());
    assertEquals("TEACHER", result.getName());
    verify(roleRepository).save(roleEntity);
  }

  @Test
  void updateRole_WhenNotExists_ThrowsException() {
    Role newRoleData = new Role();
    newRoleData.setName("TEACHER");

    when(roleRepository.findById(99)).thenReturn(Optional.empty());

    assertThrows(RoleNotFoundException.class, () -> roleStorageImpl.updateRole(99, newRoleData));
  }

  @Test
  void deleteRole_WhenExists_DeletesRole() {
    when(roleRepository.findById(1)).thenReturn(Optional.of(roleEntity));
    doNothing().when(roleRepository).delete(roleEntity);

    roleStorageImpl.deleteRole(1);

    verify(roleRepository).delete(roleEntity);
  }
}
