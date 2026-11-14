package org.vedruna.palcole.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.vedruna.palcole.domain.model.enums.ItemCondition;
import org.vedruna.palcole.domain.model.enums.ListingType;
import org.vedruna.palcole.domain.model.enums.SaleStatus;

public class AcademicItem {
  private Integer itemId;
  private Integer studentId;
  private Integer categoryId;
  private String title;
  private String description;
  private BigDecimal price = BigDecimal.ZERO;
  private ItemCondition itemCondition;
  private ListingType listingType = ListingType.VENTA;
  private SaleStatus saleStatus = SaleStatus.DISPONIBLE;
  private LocalDateTime publishedAt = LocalDateTime.now();
  private String imageUrl;

  public AcademicItem() {}

  public Integer getItemId() {
    return itemId;
  }

  public void setItemId(Integer itemId) {
    this.itemId = itemId;
  }

  public Integer getStudentId() {
    return studentId;
  }

  public void setStudentId(Integer studentId) {
    this.studentId = studentId;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public ItemCondition getItemCondition() {
    return itemCondition;
  }

  public void setItemCondition(ItemCondition itemCondition) {
    this.itemCondition = itemCondition;
  }

  public ListingType getListingType() {
    return listingType;
  }

  public void setListingType(ListingType listingType) {
    this.listingType = listingType;
  }

  public SaleStatus getSaleStatus() {
    return saleStatus;
  }

  public void setSaleStatus(SaleStatus saleStatus) {
    this.saleStatus = saleStatus;
  }

  public LocalDateTime getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(LocalDateTime publishedAt) {
    this.publishedAt = publishedAt;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
