package org.vedruna.palcole.infrastructure.adapters.inbound.dto.favoritePromotions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Representación de una promoción marcada como favorita por un usuario")
@Data
public class FavoritePromotionDTO {

  @Schema(description = "Identificador único del usuario", example = "1")
  Integer userId;

  @Schema(description = "Identificador único de la promoción favorita", example = "5")
  Integer promotionId;
}
