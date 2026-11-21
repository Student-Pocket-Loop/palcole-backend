package org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.vedruna.palcole.domain.model.enums.ItemCondition;
import org.vedruna.palcole.domain.model.enums.ListingType;
import org.vedruna.palcole.domain.model.enums.SaleStatus;

@Data
public class AcademicItemDTO {

  Integer itemId;

  Integer studentId;

  Integer categoryId;

  String title;

  String description;

  BigDecimal price;

  ItemCondition itemCondition;

  ListingType listingType;

  SaleStatus saleStatus;

  LocalDateTime publishedAt;

  String imageUrl;
}
