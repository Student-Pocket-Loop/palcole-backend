package org.vedruna.palcole.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.RoleStorage;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.RoleOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.exception.RoleNotFoundException;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.RoleEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.RoleRepository;

import lombok.AllArgsConstructor;

@Component 
@AllArgsConstructor
public class RoleStorageImpl implements RoleStorage {

  private final RoleRepository roleRepository;
    private final RoleOutConverter roleOutConverter;

  @Override
  public PageModel<Role> selectAllRoles(PageableModel pageableModel) {
    Pageable pageable = roleOutConverter.toPageable(pageableModel);
    Page<RoleEntity> studentsPage = roleRepository.findAll(pageable);
    return roleOutConverter.toStudentPageModel(studentsPage);
  }

  @Override
  public Role findRoleById(Integer roleId) {
    RoleEntity roleEntity = roleRepository.findById(roleId).orElseThrow(
        () -> new RoleNotFoundException("No se encuentra alumno con id: " + roleId)
    );
    return roleOutConverter.toDomain(
        roleEntity
    );
  }

  @Override
  public Role updateRole(Integer roleId, Role newRole) {
    RoleEntity roleEntity =
        roleRepository
            .findById(roleId)
            .orElseThrow(
                () -> new RoleNotFoundException("No se encuentra rol con id: " + roleId));

    roleEntity.setName(newRole.getName());

    return roleOutConverter.toDomain(roleRepository.save(roleEntity));
  }

  @Override
  public Role insertRole(Role newRole) {
    return roleOutConverter.toDomain(
        roleRepository.save(
            roleOutConverter.toEntity(newRole)
        )
    );
  }

  @Override
  public void deleteRole(Integer roleId) {
    if (!roleRepository.existsById(roleId)) {
      throw new RoleNotFoundException("No se encuentra rol con id: " + roleId);
    }
    roleRepository.deleteById(roleId);
  }
}
