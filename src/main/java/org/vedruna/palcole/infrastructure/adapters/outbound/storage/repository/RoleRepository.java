package org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer>{

}
