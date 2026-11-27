package org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "favorite_items")
@IdClass(FavoriteItemId.class)
public class FavoriteItemEntity {

  @Id
  @Column(name = "user_id")
  Integer userId;

  @Id
  @Column(name = "item_id")
  Integer itemId;
}
