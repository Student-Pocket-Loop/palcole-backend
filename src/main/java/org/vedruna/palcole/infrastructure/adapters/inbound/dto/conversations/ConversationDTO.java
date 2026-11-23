package org.vedruna.palcole.infrastructure.adapters.inbound.dto.conversations;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Schema(
    description =
        "Representación de una conversación entre comprador y vendedor sobre un artículo académico")
@Data
public class ConversationDTO {

  @Schema(
      description = "Identificador único de la conversación",
      example = "12",
      accessMode = Schema.AccessMode.READ_ONLY)
  Integer conversationId;

  @Schema(
      description = "Identificador del artículo académico sobre el que trata la conversación",
      example = "10")
  Integer itemId;

  @Schema(description = "Identificador del estudiante interesado (comprador)", example = "8")
  Integer buyerId;

  @Schema(
      description = "Fecha y hora de inicio de la conversación",
      example = "2026-11-23T19:15:00",
      accessMode = Schema.AccessMode.READ_ONLY)
  LocalDateTime startedAt;
}
