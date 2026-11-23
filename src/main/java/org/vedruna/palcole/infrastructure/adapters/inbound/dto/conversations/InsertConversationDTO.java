package org.vedruna.palcole.infrastructure.adapters.inbound.dto.conversations;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "Datos requeridos para iniciar una nueva conversación sobre un artículo")
@Data
public class InsertConversationDTO {

  @Schema(
      description = "Identificador del artículo académico sobre el que se consulta",
      example = "10")
  @NotNull(message = "El campo es obligatorio")
  Integer itemId;

  @Schema(description = "Identificador del estudiante interesado (comprador)", example = "8")
  @NotNull(message = "El campo es obligatorio")
  Integer buyerId;
}
