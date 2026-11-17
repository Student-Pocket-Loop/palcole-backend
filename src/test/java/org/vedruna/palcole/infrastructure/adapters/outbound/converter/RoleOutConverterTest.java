package org.vedruna.palcole.infrastructure.adapters.outbound.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.persistance.model.RoleEntity;

class RoleOutConverterTest {

  private final RoleOutConverter converter = Mappers.getMapper(RoleOutConverter.class);

  @Test
  void toDomain_MapsEntityToRole() {
    RoleEntity entity = new RoleEntity();
    entity.setUserId(1);
    entity.setName("ADMIN");

    Role domain = converter.toDomain(entity);

    assertNotNull(domain);
    assertEquals(1, domain.getRoleId());
    assertEquals("ADMIN", domain.getName());
  }

  @Test
  void toEntity_MapsRoleToEntity() {
    Role domain = new Role();
    domain.setRoleId(2);
    domain.setName("USER");

    RoleEntity entity = converter.toEntity(domain);

    assertNotNull(entity);
    assertEquals(2, entity.getUserId());
    assertEquals("USER", entity.getName());
  }
}
