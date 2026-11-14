package org.vedruna.palcole.domain.model;

import java.time.LocalDateTime;

public class Academic_items {
  private int item_id;
  private int student_id;
  private int category_id;
  private String title;
  private String description;
  private int price;
  private String item_condition;
  private String listing_type;
  private String sale_status;
  private LocalDateTime published_at;
  private String image_url;

  public Academic_items() {
    // inicializacion
  }

  public int getItem_id() {
    return item_id;
  }

  public void setItem_id(int item_id) {
    this.item_id = item_id;
  }

  public int getStudent_id() {
    return student_id;
  }

  public void setStudent_id(int student_id) {
    this.student_id = student_id;
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

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getItem_condition() {
    return item_condition;
  }

  public void setItem_condition(String item_condition) {
    this.item_condition = item_condition;
  }

  public String getListing_type() {
    return listing_type;
  }

  public void setListing_type(String listing_type) {
    this.listing_type = listing_type;
  }

  public String getSale_status() {
    return sale_status;
  }

  public void setSale_status(String sale_status) {
    this.sale_status = sale_status;
  }

  public LocalDateTime getPublished_at() {
    return published_at;
  }

  public void setPublished_at(LocalDateTime published_at) {
    this.published_at = published_at;
  }

  public String getImage_url() {
    return image_url;
  }

  public void setImage_url(String image_url) {
    this.image_url = image_url;
  }
}
