package org.vedruna.palcole.domain.model;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FavoriteItem that = (FavoriteItem) o;
    return Objects.equals(userId, that.userId) && Objects.equals(itemId, that.itemId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, itemId);
  }

}
