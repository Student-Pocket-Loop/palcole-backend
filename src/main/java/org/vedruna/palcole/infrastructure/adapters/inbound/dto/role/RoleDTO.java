package org.vedruna.palcole.infrastructure.adapters.inbound.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Representación de un rol y nivel de permisos en el sistema")
@Data
public class RoleDTO {

  @Schema(
      description = "Identificador único del rol",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  Integer roleId;

  @Schema(description = "Nombre identificativo del rol", example = "ROLE_USER")
  String name;
}
