package org.vedruna.palcole.infrastructure.adapters.outbound.storage;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Clave primaria compuesta de la tabla favorite_promotions. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritePromotionId implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer userId;
  private Integer promotionId;
}
