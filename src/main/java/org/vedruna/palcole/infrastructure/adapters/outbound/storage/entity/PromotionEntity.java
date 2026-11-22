package org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.vedruna.palcole.persistance.model.UserEntity;

@Data
@Entity
@Table(name = "promotions")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PromotionEntity {

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "promotion_id")
  Integer promotionId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = false)
  CategoryEntity category;

  @Column(name = "title", nullable = false, length = 150)
  String title;

  @Column(name = "description", columnDefinition = "TEXT")
  String description;

  @Column(name = "original_price", nullable = false, precision = 8, scale = 2)
  BigDecimal originalPrice;

  @Column(name = "discount_price", nullable = false, precision = 8, scale = 2)
  BigDecimal discountPrice;

  @Column(name = "discount_percentage", nullable = false)
  Integer discountPercentage;

  @Column(name = "promo_code", length = 50)
  String promoCode;

  @Column(name = "start_date", nullable = false)
  LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  LocalDate endDate;

  // Lado inverso de la relación N:M (la tabla puente favorite_promotions la declara el lado dueño)
  @ManyToMany(mappedBy = "favoritePromotions")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  Set<UserEntity> favoritedBy;
}
