package org.vedruna.palcole.infrastructure.adapters.inbound.dto.favoritePromotions;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "Datos requeridos para marcar una promoción como favorita")
@Data
public class InsertFavoritePromotionDTO {

  @Schema(description = "Identificador del usuario que guarda la promoción", example = "1")
  @NotNull(message = "El campo es obligatorio")
  Integer userId;

  @Schema(
      description = "Identificador de la promoción que se desea guardar como favorita",
      example = "5")
  @NotNull(message = "El campo es obligatorio")
  Integer promotionId;
}
