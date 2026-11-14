package org.vedruna.palcole.domain.model;

public class Favorite_promotions {
  private int favorite_id;
  private int user_id;
  private int promotion_id;

  public Favorite_promotions() {
    // inicializacion
  }

  public int getFavorite_id() {
    return favorite_id;
  }

  public void setFavorite_id(int favorite_id) {
    this.favorite_id = favorite_id;
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public int getPromotion_id() {
    return promotion_id;
  }

  public void setPromotion_id(int promotion_id) {
    this.promotion_id = promotion_id;
  }
}
