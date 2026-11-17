package org.vedruna.palcole.infrastructure.adapters.inbound.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.InsertRoleDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.RoleDTO;

class RoleInConverterTest {

  private final RoleInConverter converter = Mappers.getMapper(RoleInConverter.class);

  @Test
  void toDomainInsert_MapsFieldsCorrectly() {
    InsertRoleDTO dto = new InsertRoleDTO();
    dto.setName("ADMIN");

    Role domain = converter.toDomainInsert(dto);

    assertNotNull(domain);
    assertEquals("ADMIN", domain.getName());
  }

  @Test
  void toDto_MapsFieldsCorrectly() {
    Role domain = new Role();
    domain.setRoleId(1);
    domain.setName("USER");

    RoleDTO dto = converter.toDto(domain);

    assertNotNull(dto);
    assertEquals(1, dto.getRoleId());
    assertEquals("USER", dto.getName());
  }
}
