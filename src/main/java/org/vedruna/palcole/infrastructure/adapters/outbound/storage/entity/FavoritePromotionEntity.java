package org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity;

import org.vedruna.palcole.infrastructure.adapters.outbound.storage.FavoritePromotionId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "favorite_promotions")
@IdClass(FavoritePromotionId.class)
public class FavoritePromotionEntity {

  @Id
  @Column(name = "user_id")
  Integer userId;

  @Id
  @Column(name = "promotion_id")
  Integer promotionId;
}
