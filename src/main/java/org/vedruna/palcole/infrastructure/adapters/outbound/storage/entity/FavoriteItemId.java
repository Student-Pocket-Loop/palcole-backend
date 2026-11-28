package org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Clave primaria compuesta de la tabla favorite_items. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteItemId implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer userId;
  private Integer itemId;
}
