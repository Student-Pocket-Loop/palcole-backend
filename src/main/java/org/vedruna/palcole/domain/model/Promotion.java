package org.vedruna.palcole.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Promotion {
  private Integer promotionId;
  private Integer categoryId;
  private String title;
  private String description;
  private BigDecimal originalPrice;
  private BigDecimal discountPrice;
  private int discountPercentage;
  private String promoCode;
  private LocalDate startDate;
  private LocalDate endDate;

  public Promotion() {}

  public Integer getPromotionId() {
    return promotionId;
  }

  public void setPromotionId(Integer promotionId) {
    this.promotionId = promotionId;
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

  public BigDecimal getOriginalPrice() {
    return originalPrice;
  }

  public void setOriginalPrice(BigDecimal originalPrice) {
    this.originalPrice = originalPrice;
  }

  public BigDecimal getDiscountPrice() {
    return discountPrice;
  }

  public void setDiscountPrice(BigDecimal discountPrice) {
    this.discountPrice = discountPrice;
  }

  public int getDiscountPercentage() {
    return discountPercentage;
  }

  public void setDiscountPercentage(int discountPercentage) {
    this.discountPercentage = discountPercentage;
  }

  public String getPromoCode() {
    return promoCode;
  }

  public void setPromoCode(String promoCode) {
    this.promoCode = promoCode;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Promotion that = (Promotion) o;
    return Objects.equals(promotionId, that.promotionId)
        && Objects.equals(categoryId, that.categoryId)
        && Objects.equals(title, that.title)
        && Objects.equals(description, that.description)
        && Objects.equals(originalPrice, that.originalPrice)
        && Objects.equals(discountPrice, that.discountPrice)
        && Objects.equals(discountPercentage, that.discountPercentage)
        && Objects.equals(promoCode, that.promoCode)
        && Objects.equals(startDate, that.startDate)
        && Objects.equals(endDate, that.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        promotionId,
        categoryId,
        title,
        description,
        originalPrice,
        discountPrice,
        discountPercentage,
        promoCode,
        startDate,
        endDate);
  }
}
