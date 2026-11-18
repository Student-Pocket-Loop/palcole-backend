package org.vedruna.palcole.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
  private Integer userId;
  private Integer rolesRoleId;
  private String username;
  private String email;
  private String password;
  private String educationalCenter = "Santa Joaquina de Vedruna";
  private LocalDateTime createDate = LocalDateTime.now();
  private LocalDateTime accountExpirationDate;
  private Boolean isLocked = false;
  private LocalDateTime credentialsExpirationDate;
  private Boolean isEnabled = true;

  public User() {}

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getRolesRoleId() {
    return rolesRoleId;
  }

  public void setRolesRoleId(Integer rolesRoleId) {
    this.rolesRoleId = rolesRoleId;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEducationalCenter() {
    return educationalCenter;
  }

  public void setEducationalCenter(String educationalCenter) {
    this.educationalCenter = educationalCenter;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  public LocalDateTime getAccountExpirationDate() {
    return accountExpirationDate;
  }

  public void setAccountExpirationDate(LocalDateTime accountExpirationDate) {
    this.accountExpirationDate = accountExpirationDate;
  }

  public boolean isLocked() {
    return isLocked;
  }

  public void setLocked(boolean locked) {
    isLocked = locked;
  }

  public LocalDateTime getCredentialsExpirationDate() {
    return credentialsExpirationDate;
  }

  public void setCredentialsExpirationDate(LocalDateTime credentialsExpirationDate) {
    this.credentialsExpirationDate = credentialsExpirationDate;
  }

  public boolean isEnabled() {
    return isEnabled;
  }

  public void setEnabled(boolean enabled) {
    isEnabled = enabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return isLocked == user.isLocked
        && isEnabled == user.isEnabled
        && Objects.equals(userId, user.userId)
        && Objects.equals(rolesRoleId, user.rolesRoleId)
        && Objects.equals(username, user.username)
        && Objects.equals(email, user.email)
        && Objects.equals(password, user.password)
        && Objects.equals(educationalCenter, user.educationalCenter)
        && Objects.equals(createDate, user.createDate)
        && Objects.equals(accountExpirationDate, user.accountExpirationDate)
        && Objects.equals(credentialsExpirationDate, user.credentialsExpirationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        userId,
        rolesRoleId,
        username,
        email,
        password,
        educationalCenter,
        createDate,
        accountExpirationDate,
        isLocked,
        credentialsExpirationDate,
        isEnabled);
  }
}
