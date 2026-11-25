package org.vedruna.palcole.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vedruna.palcole.controller.dto.MainDto;

@Tag(name = "Main", description = "Endpoints de prueba de acceso público, privado y administrador")
@Validated
@RequestMapping("/api/v1/main")
public interface MainController {

  @Operation(
      summary = "Endpoint público",
      description = "Accesible por cualquier usuario sin necesidad de autenticación.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Respuesta pública obtenida correctamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MainDto.class),
                    examples = {
                      @ExampleObject(
                          name = "Acceso Público",
                          summary = "Respuesta sin token",
                          value =
                              """
                              {
                                "message": "Bienvenido a la zona pública de PalCole"
                              }
                              """)
                    }))
      })
  @SecurityRequirements() // Público: no requiere token en Swagger
  @GetMapping("/public")
  public ResponseEntity<MainDto> index();

  @Operation(
      summary = "Endpoint privado (Usuarios autenticados)",
      description =
          "Accesible para cualquier usuario autenticado con token válido (ROLE_USER o ROLE_ADMIN).")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Acceso concedido a usuario autenticado",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MainDto.class),
                    examples = {
                      @ExampleObject(
                          name = "Acceso Usuario Normal (manuel)",
                          summary = "Respuesta obtenida con token de ROLE_USER",
                          value =
                              """
                              {
                                "message": "Bienvenido a la zona privada, usuario autenticado"
                              }
                              """),
                      @ExampleObject(
                          name = "Acceso Administrador (joaquin)",
                          summary = "Respuesta obtenida con token de ROLE_ADMIN",
                          value =
                              """
                              {
                                "message": "Bienvenido a la zona privada, administrador"
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT ausente, inválido o expirado",
            content = @Content)
      })
  @GetMapping("/private")
  public ResponseEntity<MainDto> privateIndex();

  @Operation(
      summary = "Endpoint protegido (Solo ADMIN)",
      description = "Restringido exclusivamente a usuarios con rol ROLE_ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Acceso concedido al administrador",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MainDto.class),
                    examples = {
                      @ExampleObject(
                          name = "Acceso Administrador (joaquin)",
                          summary = "Respuesta exclusiva para ROLE_ADMIN",
                          value =
                              """
                              {
                                "message": "Bienvenido al panel de administración"
                              }
                              """)
                    })),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT ausente, inválido o expirado",
            content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "Acceso denegado: se requiere rol ROLE_ADMIN (ej. usuario manuel)",
            content = @Content)
      })
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/admin")
  public ResponseEntity<MainDto> adminIndex();
}
