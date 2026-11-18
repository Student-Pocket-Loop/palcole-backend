package org.vedruna.palcole.domain.model;

import java.time.LocalDate;
import java.util.Objects;

public class User {
  private Integer userId;
  private Integer roleId;
  private String username;
  private String email;
  private String passwordHash;
  private String educationalCenter = "Santa Joaquina de Vedruna";
  private boolean verified = false;
  private LocalDate registrationDate = LocalDate.now();
  private LocalDate expirationDate;
  private boolean active = true;

  public User() {}

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
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

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getEducationalCenter() {
    return educationalCenter;
  }

  public void setEducationalCenter(String educationalCenter) {
    this.educationalCenter = educationalCenter;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

  public LocalDate getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(LocalDate registrationDate) {
    this.registrationDate = registrationDate;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(LocalDate expirationDate) {
    this.expirationDate = expirationDate;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return verified == user.verified
        && active == user.active
        && Objects.equals(userId, user.userId)
        && Objects.equals(roleId, user.roleId)
        && Objects.equals(username, user.username)
        && Objects.equals(email, user.email)
        && Objects.equals(passwordHash, user.passwordHash)
        && Objects.equals(educationalCenter, user.educationalCenter)
        && Objects.equals(registrationDate, user.registrationDate)
        && Objects.equals(expirationDate, user.expirationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        userId,
        roleId,
        username,
        email,
        passwordHash,
        educationalCenter,
        verified,
        registrationDate,
        expirationDate,
        active);
  }

}
