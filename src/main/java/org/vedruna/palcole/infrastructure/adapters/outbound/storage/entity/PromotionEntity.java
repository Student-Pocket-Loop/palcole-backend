package org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "promotions")
public class PromotionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "promotion_id")
  Integer promotionId;

  @Column(name = "category_id", nullable = false)
  Integer categoryId;

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
}
