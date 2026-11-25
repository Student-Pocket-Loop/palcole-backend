package org.vedruna.palcole.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.vedruna.palcole.domain.exception.roles.RoleNotFoundException;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.RoleOutConverter;
import org.vedruna.palcole.persistance.model.RoleEntity;
import org.vedruna.palcole.persistance.repository.RoleRepository;

@Component
@AllArgsConstructor
public class RoleStorageImpl implements RoleStorage {

  private final RoleRepository roleRepository;
  private final RoleOutConverter roleOutConverter;

  @Override
  @Cacheable(
      value = "rolesPage",
      key = "#pageableModel.page + '-' + #pageableModel.size + '-' + #pageableModel.sort")
  public PageModel<Role> selectAllRoles(PageableModel pageableModel) {
    Pageable pageable = roleOutConverter.toPageable(pageableModel);
    Page<RoleEntity> studentsPage = roleRepository.findAll(pageable);
    return roleOutConverter.toRolePageModel(studentsPage);
  }

  @Override
  @Cacheable(value = "roles", key = "#roleId")
  public Role findRoleById(Integer roleId) {
    RoleEntity roleEntity =
        roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException(roleId));
    return roleOutConverter.toDomain(roleEntity);
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(value = "roles", key = "#roleId"),
        @CacheEvict(value = "rolesPage", allEntries = true)
      })
  public Role updateRole(Integer roleId, Role newRole) {
    RoleEntity roleEntity =
        roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException(roleId));

    roleEntity.setName(newRole.getName());

    return roleOutConverter.toDomain(roleRepository.save(roleEntity));
  }

  @Override
  @CacheEvict(value = "rolesPage", allEntries = true)
  public Role insertRole(Role newRole) {
    return roleOutConverter.toDomain(roleRepository.save(roleOutConverter.toEntity(newRole)));
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(value = "roles", key = "#roleId"),
        @CacheEvict(value = "rolesPage", allEntries = true)
      })
  public void deleteRole(Integer roleId) {
    if (!roleRepository.existsById(roleId)) {
      throw new RoleNotFoundException(roleId);
    }
    roleRepository.deleteById(roleId);
  }
}
