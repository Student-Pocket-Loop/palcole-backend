package org.vedruna.palcole.domain.model;

import java.time.LocalDate;

public class Users {
  private int user_id;
  private int role_id;
  private String username;
  private String email;
  private String password_hash;
  private String educational_center;
  private boolean is_verified;
  private LocalDate registration_date;
  private LocalDate expiration_date;
  private boolean is_active;

  public Users() {
    // inicializacion
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public int getRole_id() {
    return role_id;
  }

  public void setRole_id(int role_id) {
    this.role_id = role_id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword_hash() {
    return password_hash;
  }

  public void setPassword_hash(String password_hash) {
    this.password_hash = password_hash;
  }

  public String getEducational_center() {
    return educational_center;
  }

  public void setEducational_center(String educational_center) {
    this.educational_center = educational_center;
  }

  public boolean isIs_verified() {
    return is_verified;
  }

  public void setIs_verified(boolean is_verified) {
    this.is_verified = is_verified;
  }

  public LocalDate getRegistration_date() {
    return registration_date;
  }

  public void setRegistration_date(LocalDate registration_date) {
    this.registration_date = registration_date;
  }

  public LocalDate getExpiration_date() {
    return expiration_date;
  }

  public void setExpiration_date(LocalDate expiration_date) {
    this.expiration_date = expiration_date;
  }

  public boolean isIs_active() {
    return is_active;
  }

  public void setIs_active(boolean is_active) {
    this.is_active = is_active;
  }
}
