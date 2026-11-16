package org.vedruna.palcole.domain.model;

public class FavoriteItem {
  private Integer favoriteId;
  private Integer userId;
  private Integer itemId;

  public FavoriteItem() {}

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

  public Integer getItemId() {
    return itemId;
  }

  public void setItemId(Integer itemId) {
    this.itemId = itemId;
  }
}
