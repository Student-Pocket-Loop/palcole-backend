package org.vedruna.palcole.persistance.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vedruna.palcole.persistance.model.RolEntity;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Integer> {
  Optional<RolEntity> findByRolName(String rolName);
}
