package org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.vedruna.palcole.domain.model.enums.ItemCondition;
import org.vedruna.palcole.domain.model.enums.ListingType;
import org.vedruna.palcole.domain.model.enums.SaleStatus;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.ItemConditionConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.ListingTypeConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.SaleStatusConverter;

@Data
@Entity
@Table(name = "academic_items")
public class AcademicItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  Integer itemId;

  @Column(name = "student_id", nullable = false)
  Integer studentId;

  @Column(name = "category_id", nullable = false)
  Integer categoryId;

  @Column(name = "title", nullable = false, length = 150)
  String title;

  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  String description;

  @Column(name = "price", precision = 8, scale = 2)
  BigDecimal price;

  @Convert(converter = ItemConditionConverter.class)
  @Column(
      name = "item_condition",
      nullable = false,
      columnDefinition = "enum('nuevo','como_nuevo','usado','deteriorado')")
  ItemCondition itemCondition;

  @Convert(converter = ListingTypeConverter.class)
  @Column(
      name = "listing_type",
      nullable = false,
      columnDefinition = "enum('venta','intercambio','regalo')")
  ListingType listingType;

  @Convert(converter = SaleStatusConverter.class)
  @Column(
      name = "sale_status",
      nullable = false,
      columnDefinition = "enum('disponible','reservado','entregado')")
  SaleStatus saleStatus;

  @Column(name = "published_at")
  LocalDateTime publishedAt;

  @Column(name = "image_url", nullable = false, length = 150)
  String imageUrl;
}
