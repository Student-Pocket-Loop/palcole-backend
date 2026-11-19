package org.vedruna.palcole.infrastructure.adapters.inbound.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.user.DeleteUserUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.NewUserUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.SelectAllUsersUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.SelectUserByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.UpdateUserUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.UserInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.user.InsertUserDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.user.UserDTO;

@CrossOrigin
@AllArgsConstructor
@Validated
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final NewUserUseCase newUserUseCase;
  private final DeleteUserUseCase deleteUserUseCase;
  private final SelectAllUsersUseCase selectAllUsersUseCase;
  private final SelectUserByIdUseCase selectUserByIdUseCase;
  private final UpdateUserUseCase updateUserUseCase;
  private final UserInConverter userInConverter;

  @GetMapping("/")
  public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
    PageableModel pageableModel = userInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        userInConverter.toUsersPage(
            selectAllUsersUseCase.selectAllUsers(pageableModel), pageableModel));
  }

  @PostMapping("/")
  public ResponseEntity<UserDTO> insertIntoUser(@RequestBody @Valid InsertUserDTO insertUserDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            userInConverter.toDto(
                newUserUseCase.newUser(userInConverter.toDomainInsert(insertUserDTO))));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Integer userId) {
    return ResponseEntity.ok(userInConverter.toDto(selectUserByIdUseCase.selectUserById(userId)));
  }

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @PutMapping("/{userId}")
  public ResponseEntity<UserDTO> updateUser(
      @PathVariable Integer userId, @RequestBody @Valid InsertUserDTO insertUserDTO) {
    return ResponseEntity.ok(
        userInConverter.toDto(
            updateUserUseCase.updateUser(userId, userInConverter.toDomainInsert(insertUserDTO))));
  }

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
    deleteUserUseCase.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }
}
