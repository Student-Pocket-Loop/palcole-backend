package org.vedruna.palcole.domain.model;

public class Favorite_items {
  private int favorite_id;
  private int user_id;
  private int item_id;

  public Favorite_items() {
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

  public int getItem_id() {
    return item_id;
  }

  public void setItem_id(int item_id) {
    this.item_id = item_id;
  }
}
