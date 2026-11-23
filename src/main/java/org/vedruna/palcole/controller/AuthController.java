package org.vedruna.palcole.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos o formato incorrecto",
            content = @Content),
        @ApiResponse(
            responseCode = "409",
            description = "El usuario o correo electrónico ya existe",
            content = @Content)
      })
  @SecurityRequirements() // Indica en Swagger que este endpoint es público (no pide token)
  @PostMapping(value = "/register")
  ResponseEntity<UserDto> register(@RequestBody @Valid RegisterRequestDto request);

  @Operation(
      summary = "Iniciar sesión",
      description =
          "Autentica al usuario con sus credenciales y devuelve el Access Token y Refresh Token JWT.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autenticación exitosa",
            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Faltan campos obligatorios",
            content = @Content),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales incorrectas",
            content = @Content)
      })
  @SecurityRequirements() // Endpoint público (no pide token)
  @PostMapping(value = "/login")
  ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto request);

  @Operation(
      summary = "Obtener perfil del usuario actual",
      description = "Devuelve la información del usuario autenticado extraída de su token JWT.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Datos del usuario recuperados correctamente",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
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
            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Refresh token inválido o expirado",
            content = @Content)
      })
  @SecurityRequirements() // Endpoint público (no pide cabecera Bearer, recibe el token en el body)
  @PostMapping("/refresh")
  ResponseEntity<AuthResponseDto> refreshToken(@RequestBody @Valid RefreshRequestDto request);
}
