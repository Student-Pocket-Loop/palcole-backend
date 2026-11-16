package org.vedruna.palcole.domain.model;

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
}
