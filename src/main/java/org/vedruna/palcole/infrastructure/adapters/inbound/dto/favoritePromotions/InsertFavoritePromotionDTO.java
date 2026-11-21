package org.vedruna.palcole.infrastructure.adapters.inbound.dto.favoritePromotions;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InsertFavoritePromotionDTO {

  @NotNull(message = "El campo es obligatorio")
  Integer userId;

  @NotNull(message = "El campo es obligatorio")
  Integer promotionId;
}
