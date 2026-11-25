package org.vedruna.palcole.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vedruna.palcole.controller.dto.UserDto;
import org.vedruna.palcole.controller.dto.authdto.AuthResponseDto;
import org.vedruna.palcole.controller.dto.authdto.LoginRequestDto;
import org.vedruna.palcole.controller.dto.authdto.RefreshRequestDto;
import org.vedruna.palcole.controller.dto.authdto.RegisterRequestDto;
import org.vedruna.palcole.persistance.model.UserEntity;

@Tag(name = "Auth", description = "Authorization and authentication endpoints")
@Validated
@RequestMapping("/api/v1/auth")
public interface AuthController {

  @Operation(
      summary = "Registrar un nuevo usuario",
      description = "Crea una nueva cuenta de usuario en el sistema y devuelve sus datos básicos.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario registrado correctamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class),
                    examples = {
                      @ExampleObject(
                          name = "Usuario registrado",
                          summary = "Respuesta tras registrar a joaquin",
                          value =
                              """
                              {
                                "userId": 1,
                                "rolesRoleId": 2,
                                "username": "joaquin",
                                "email": "joaquin@localhost.com",
                                "educationalCenter": null,
                                "createDate": "2026-09-01T10:00:00",
                                "accountExpirationDate": null,
                                "isLocked": false,
                                "credentialsExpirationDate": null,
                                "isEnabled": true
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos o formato incorrecto",
            content = @Content),
        @ApiResponse(
            responseCode = "409",
            description = "El usuario o correo electrónico ya existe",
            content = @Content)
      })
  @SecurityRequirements()
  @PostMapping(value = "/register")
  ResponseEntity<UserDto> register(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Datos de registro del nuevo usuario",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = RegisterRequestDto.class),
                      examples = {
                        @ExampleObject(
                            name = "Registro Ejemplo",
                            summary = "Datos de registro de joaquin",
                            value =
                                """
                                {
                                    "username": "joaquin",
                                    "password": "Abcd1234",
                                    "email": "joaquin@localhost.com"
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          RegisterRequestDto request);

  @Operation(
      summary = "Iniciar sesión",
      description =
          "Autentica al usuario con sus credenciales y devuelve el Access Token y Refresh Token JWT.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autenticación exitosa",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponseDto.class),
                    examples = {
                      @ExampleObject(
                          name = "Respuesta Login Admin (joaquin)",
                          summary = "Tokens devueltos para el administrador",
                          value =
                              """
                              {
                                "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.admin_joaquin_token...",
                                "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.admin_refresh_token..."
                              }
                              """),
                      @ExampleObject(
                          name = "Respuesta Login User (manuel)",
                          summary = "Tokens devueltos para un usuario estándar",
                          value =
                              """
                              {
                                "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.user_manuel_token...",
                                "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.user_refresh_token..."
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "400",
            description = "Faltan campos obligatorios",
            content = @Content),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales incorrectas",
            content = @Content)
      })
  @SecurityRequirements()
  @PostMapping(value = "/login")
  ResponseEntity<AuthResponseDto> login(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Credenciales de acceso (elige entre Admin o Usuario Normal)",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = LoginRequestDto.class),
                      examples = {
                        @ExampleObject(
                            name = "LOGIN ADMIN (joaquin)",
                            summary = "Credenciales de administrador",
                            value =
                                """
                                {
                                    "username": "joaquin",
                                    "password": "Abcd1234"
                                }
                                """),
                        @ExampleObject(
                            name = "LOGIN USER (manuel)",
                            summary = "Credenciales de usuario normal",
                            value =
                                """
                                {
                                    "username": "manuel",
                                    "password": "Abcd1234"
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          LoginRequestDto request);

  @Operation(
      summary = "Obtener perfil del usuario actual",
      description = "Devuelve la información del usuario autenticado extraída de su token JWT.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Datos del usuario recuperados correctamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class),
                    examples = {
                      @ExampleObject(
                          name = "Perfil Usuario Normal (manuel)",
                          summary = "Datos devueltos al consultar /me con token de usuario",
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
                              """),
                      @ExampleObject(
                          name = "Perfil Administrador (joaquin)",
                          summary = "Datos devueltos al consultar /me con token de admin",
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
                              """)
                    })),
        @ApiResponse(
            responseCode = "401",
            description = "Token ausente, inválido o expirado",
            content = @Content)
      })
  @GetMapping(value = "/me")
  UserDto me(@Parameter(hidden = true) @AuthenticationPrincipal UserEntity userLogueado);

  @Operation(
      summary = "Refrescar token de acceso",
      description = "Genera un nuevo Access Token válido utilizando un Refresh Token activo.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token refrescado exitosamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponseDto.class),
                    examples = {
                      @ExampleObject(
                          name = "Tokens renovados",
                          summary = "Nuevo par de tokens generado",
                          value =
                              """
                              {
                                "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.new_access_token...",
                                "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.new_refresh_token..."
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "401",
            description = "Refresh token inválido o expirado",
            content = @Content)
      })
  @SecurityRequirements()
  @PostMapping("/refresh")
  ResponseEntity<AuthResponseDto> refreshToken(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Refresh token obtenido en el inicio de sesión",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = RefreshRequestDto.class),
                      examples = {
                        @ExampleObject(
                            name = "Refresh Token",
                            summary = "Ejemplo de petición de refresco",
                            value =
                                """
                                {
                                    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh_token..."
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          RefreshRequestDto request);
}
