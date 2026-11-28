package org.vedruna.palcole.infrastructure.adapters.inbound.dto.favouriteitem;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InsertFavoriteItemDTO {

  @NotNull(message = "El campo es obligatorio")
  Integer userId;

  @NotNull(message = "El campo es obligatorio")
  Integer itemId;
}
