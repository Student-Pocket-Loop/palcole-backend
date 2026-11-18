package org.vedruna.palcole.domain.model;

import java.util.Objects;

public class Role {
  private Integer roleId;
  private String name;

  public Role() {}

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Role role = (Role) o;
    return Objects.equals(roleId, role.roleId) && Objects.equals(name, role.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roleId, name);
  }
}
