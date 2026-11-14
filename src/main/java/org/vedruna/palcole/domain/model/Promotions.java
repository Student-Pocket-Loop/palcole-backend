package org.vedruna.palcole.domain.model;

import java.time.LocalDate;

public class Promotions {
  private int promotion_id;
  private int category_id;
  private String title;
  private String description;
  private int original_price;
  private int discount_price;
  private int discount_percentage;
  private String promo_code;
  private LocalDate start_date;
  private LocalDate end_date;

  public Promotions() {
    // inicializacion
  }

  public int getPromotion_id() {
    return promotion_id;
  }

  public void setPromotion_id(int promotion_id) {
    this.promotion_id = promotion_id;
  }

  public int getCategory_id() {
    return category_id;
  }

  public void setCategory_id(int category_id) {
    this.category_id = category_id;
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

  public int getOriginal_price() {
    return original_price;
  }

  public void setOriginal_price(int original_price) {
    this.original_price = original_price;
  }

  public int getDiscount_price() {
    return discount_price;
  }

  public void setDiscount_price(int discount_price) {
    this.discount_price = discount_price;
  }

  public int getDiscount_percentage() {
    return discount_percentage;
  }

  public void setDiscount_percentage(int discount_percentage) {
    this.discount_percentage = discount_percentage;
  }

  public String getPromo_code() {
    return promo_code;
  }

  public void setPromo_code(String promo_code) {
    this.promo_code = promo_code;
  }

  public LocalDate getStart_date() {
    return start_date;
  }

  public void setStart_date(LocalDate start_date) {
    this.start_date = start_date;
  }

  public LocalDate getEnd_date() {
    return end_date;
  }

  public void setEnd_date(LocalDate end_date) {
    this.end_date = end_date;
  }
}
