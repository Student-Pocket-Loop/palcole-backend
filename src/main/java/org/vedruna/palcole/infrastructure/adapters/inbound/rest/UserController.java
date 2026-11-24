package org.vedruna.palcole.infrastructure.adapters.inbound.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import org.vedruna.palcole.domain.ports.inbound.user.ChangePasswordUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.DeleteUserUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.NewUserUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.SelectAllUsersUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.SelectUserByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.user.UpdateUserUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.UserInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.ChangePasswordDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.user.InsertUserDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.user.UserDTO;
import org.vedruna.palcole.persistance.model.UserEntity;

@Tag(
    name = "Users",
    description = "Gestión y administración de cuentas de usuario en la plataforma")
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
  private final ChangePasswordUseCase changePasswordUseCase;

  @Operation(
      summary = "Listar todos los usuarios (paginado)",
      description =
          "Devuelve una página con los usuarios registrados en el sistema. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista paginada de usuarios recuperada correctamente"),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de paginación u ordenación inválidos",
            content = @Content),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado (token JWT ausente o inválido)",
            content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol ADMIN, denegado para ROLE_USER)",
            content = @Content)
      })
  @GetMapping("/")
  public ResponseEntity<Page<UserDTO>> getAllUsers(@ParameterObject Pageable pageable) {
    PageableModel pageableModel = userInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        userInConverter.toUsersPage(
            selectAllUsersUseCase.selectAllUsers(pageableModel), pageableModel));
  }

  @Operation(
      summary = "Crear un nuevo usuario",
      description =
          "Registra manualmente una nueva cuenta de usuario con su rol asignado. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario creado exitosamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Usuario creado (alumno1)",
                          summary = "Respuesta tras registrar al alumno1 desde administración",
                          value =
                              """
                              {
                                "userId": 3,
                                "rolesRoleId": 2,
                                "username": "alumno1",
                                "email": "alumno1@localhost.es",
                                "educationalCenter": "Santa Joaquina de Vedruna",
                                "createDate": "2026-09-01T15:00:00",
                                "accountExpirationDate": null,
                                "isLocked": false,
                                "credentialsExpirationDate": null,
                                "isEnabled": true
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos o campos obligatorios ausentes",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol ADMIN, denegado para ROLE_USER)",
            content = @Content),
        @ApiResponse(
            responseCode = "409",
            description = "El nombre de usuario o correo electrónico ya está en uso",
            content = @Content)
      })
  @PostMapping("/")
  public ResponseEntity<UserDTO> insertIntoUser(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Datos del nuevo usuario a registrar",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = InsertUserDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Crear Alumno (alumno1)",
                            summary = "Ejemplo de creación de usuario desde request.http",
                            value =
                                """
                                {
                                    "username": "alumno1",
                                    "email": "alumno1@localhost.es",
                                    "password": "Abcd1234",
                                    "educationalCenter": "Santa Joaquina de Vedruna"
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          InsertUserDTO insertUserDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            userInConverter.toDto(
                newUserUseCase.newUser(userInConverter.toDomainInsert(insertUserDTO))));
  }

  @Operation(
      summary = "Obtener un usuario por su ID",
      description =
          "Busca y devuelve los detalles de un usuario específico a partir de su identificador. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Perfil Administrador (joaquin - ID 1)",
                          summary = "Respuesta cuando se consulta un usuario con ROLE_ADMIN",
                          value =
                              """
                              {
                                "userId": 1,
                                "rolesRoleId": 1,
                                "username": "joaquin",
                                "email": "joaquin@localhost.com",
                                "educationalCenter": "Santa Joaquina de Vedruna",
                                "createDate": "2026-09-01T10:00:00",
                                "accountExpirationDate": null,
                                "isLocked": false,
                                "credentialsExpirationDate": null,
                                "isEnabled": true
                              }
                              """),
                      @ExampleObject(
                          name = "Perfil Usuario Normal (manuel - ID 2)",
                          summary = "Respuesta cuando se consulta un usuario con ROLE_USER",
                          value =
                              """
                              {
                                "userId": 2,
                                "rolesRoleId": 2,
                                "username": "manuel",
                                "email": "manuel@localhost.com",
                                "educationalCenter": "Santa Joaquina de Vedruna",
                                "createDate": "2026-09-01T10:15:00",
                                "accountExpirationDate": null,
                                "isLocked": false,
                                "credentialsExpirationDate": null,
                                "isEnabled": true
                              }
                              """)
                    })),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol ADMIN, denegado para ROLE_USER)",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ningún usuario con el ID proporcionado",
            content = @Content)
      })
  @GetMapping("/{userId}")
  public ResponseEntity<UserDTO> getUserById(
      @Parameter(description = "ID único del usuario", example = "1") @PathVariable
          Integer userId) {
    return ResponseEntity.ok(userInConverter.toDto(selectUserByIdUseCase.selectUserById(userId)));
  }

  @Operation(
      summary = "Actualizar un usuario",
      description = "Modifica los datos de un usuario existente. Requiere rol USER o ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario actualizado correctamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Usuario actualizado (alumno1_editado)",
                          summary = "Respuesta tras actualizar perfil y estado de cuenta",
                          value =
                              """
                              {
                                "userId": 2,
                                "rolesRoleId": 2,
                                "username": "alumno1_editado",
                                "email": "alumno1@localhost.es",
                                "educationalCenter": "Santa Joaquina de Vedruna",
                                "createDate": "2026-09-01T15:00:00",
                                "accountExpirationDate": null,
                                "isLocked": false,
                                "credentialsExpirationDate": null,
                                "isEnabled": true
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de actualización inválidos",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol USER o ADMIN)",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ningún usuario con el ID proporcionado",
            content = @Content)
      })
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @PutMapping("/{userId}")
  public ResponseEntity<UserDTO> updateUser(
      @Parameter(description = "ID único del usuario a actualizar", example = "2") @PathVariable
          Integer userId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Datos actualizados del usuario",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = InsertUserDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Editar Alumno (alumno1_editado)",
                            summary = "Ejemplo de actualización desde request.http",
                            value =
                                """
                                {
                                    "roleId": 2,
                                    "username": "alumno1_editado",
                                    "email": "alumno1@localhost.es",
                                    "password": "Abcd1234",
                                    "educationalCenter": "Santa Joaquina de Vedruna",
                                    "isLocked": false,
                                    "isEnabled": true
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          InsertUserDTO insertUserDTO) {
    return ResponseEntity.ok(
        userInConverter.toDto(
            updateUserUseCase.updateUser(userId, userInConverter.toDomainInsert(insertUserDTO))));
  }

  @Operation(
      summary = "Eliminar un usuario",
      description = "Elimina permanentemente un usuario por su ID. Requiere rol USER o ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "Usuario eliminado correctamente (sin contenido)",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol USER o ADMIN)",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ningún usuario con el ID proporcionado",
            content = @Content)
      })
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "ID único del usuario a eliminar", example = "3") @PathVariable
          Integer userId) {
    deleteUserUseCase.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(
        summary = "Cambiar la contraseña del usuario autenticado",
        description = "Recibe la contraseña actual y la nueva. Requiere rol USER o ADMIN.")
    @ApiResponses(
        value = {
        @ApiResponse(responseCode = "204", description = "Contraseña cambiada", content = @Content),
        @ApiResponse(
            responseCode = "400",
            description = "Contraseña actual incorrecta, nueva inválida o igual a la actual",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
        })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(
        @Parameter(hidden = true) @AuthenticationPrincipal UserEntity loggedUser,
        @RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
    changePasswordUseCase.changePassword(
        loggedUser.getUserId(),
        changePasswordDTO.getCurrentPassword(),
        changePasswordDTO.getNewPassword());
    return ResponseEntity.noContent().build();
    }

}
