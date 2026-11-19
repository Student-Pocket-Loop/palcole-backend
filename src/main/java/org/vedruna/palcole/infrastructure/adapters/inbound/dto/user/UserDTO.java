package org.vedruna.palcole.infrastructure.adapters.inbound.dto.user;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserDTO {

  Integer userId;
  Integer rolesRoleId;
  String username;
  String email;
  String password;
  String educationalCenter;
  LocalDateTime createDate;
  LocalDateTime accountExpirationDate;
  Boolean isLocked;
  LocalDateTime credentialsExpirationDate;
  Boolean isEnabled;
}
