package org.vedruna.palcole.infrastructure.adapters.inbound.dto.promotions;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class PromotionDTO {

  Integer promotionId;

  Integer categoryId;

  String title;

  String description;

  BigDecimal originalPrice;

  BigDecimal discountPrice;

  Integer discountPercentage;

  String promoCode;

  LocalDate startDate;

  LocalDate endDate;
}
