package org.vedruna.palcole.domain.model;

import java.util.Objects;

public class FavoritePromotion {
  private Integer favoriteId;
  private Integer userId;
  private Integer promotionId;

  public FavoritePromotion() {}

  public Integer getFavoriteId() {
    return favoriteId;
  }

  public void setFavoriteId(Integer favoriteId) {
    this.favoriteId = favoriteId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getPromotionId() {
    return promotionId;
  }

  public void setPromotionId(Integer promotionId) {
    this.promotionId = promotionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FavoritePromotion that = (FavoritePromotion) o;
    return Objects.equals(userId, that.userId) && Objects.equals(promotionId, that.promotionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, promotionId);
  }

}
