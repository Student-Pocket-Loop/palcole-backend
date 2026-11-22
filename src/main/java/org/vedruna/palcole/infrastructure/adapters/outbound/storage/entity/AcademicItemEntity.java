package org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.vedruna.palcole.domain.model.enums.ItemCondition;
import org.vedruna.palcole.domain.model.enums.ListingType;
import org.vedruna.palcole.domain.model.enums.SaleStatus;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.ItemConditionConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.ListingTypeConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.SaleStatusConverter;
import org.vedruna.palcole.persistance.model.UserEntity;

@Data
@Entity
@Table(name = "academic_items")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AcademicItemEntity {

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  Integer itemId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "student_id", referencedColumnName = "user_id", nullable = false)
  UserEntity student;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = false)
  CategoryEntity category;

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

  // La FK conversations.item_id es ON DELETE CASCADE: lo hace la base de datos
  @OneToMany(mappedBy = "item")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  List<ConversationEntity> conversations;

  // Lado inverso de la relación N:M (la tabla puente favorite_items la declara el lado dueño)
  @ManyToMany(mappedBy = "favoriteItems")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  Set<UserEntity> favoritedBy;
}
