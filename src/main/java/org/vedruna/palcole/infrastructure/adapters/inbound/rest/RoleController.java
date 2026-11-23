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
import org.vedruna.palcole.domain.ports.inbound.role.DeleteRoleUseCase;
import org.vedruna.palcole.domain.ports.inbound.role.NewRoleUseCase;
import org.vedruna.palcole.domain.ports.inbound.role.SelectAllRolesUseCase;
import org.vedruna.palcole.domain.ports.inbound.role.SelectRoleByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.role.UpdateRoleUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.RoleInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.role.InsertRoleDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.role.RoleDTO;

@Tag(
    name = "Roles",
    description =
        "Gestión de roles y permisos de acceso del sistema (exclusivo para administradores)")
@CrossOrigin
@AllArgsConstructor
@Validated
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

  private final NewRoleUseCase newRoleUseCase;
  private final DeleteRoleUseCase deleteRoleUseCase;
  private final SelectAllRolesUseCase selectAllRolesUseCase;
  private final SelectRoleByIdUseCase selectRoleByIdUseCase;
  private final UpdateRoleUseCase updateRoleUseCase;
  private final RoleInConverter roleInConverter;

  @Operation(
      summary = "Listar todos los roles (paginado)",
      description =
          "Devuelve una página con los roles registrados en el sistema. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista paginada de roles recuperada correctamente"),
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
  public ResponseEntity<Page<RoleDTO>> getAllRoles(@ParameterObject Pageable pageable) {
    PageableModel pageableModel = roleInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        roleInConverter.toRolesPage(
            selectAllRolesUseCase.selectAllRoles(pageableModel), pageableModel));
  }

  @Operation(
      summary = "Crear un nuevo rol",
      description = "Registra un nuevo rol de usuario en el sistema. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Rol creado exitosamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RoleDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Rol creado (ADMIN)",
                          summary = "Respuesta tras crear el rol NUEVO con ROLE_ADMIN",
                          value =
                              """
                              {
                                "roleId": 3,
                                "name": "NUEVO"
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
            content = @Content)
      })
  @PostMapping("/")
  public ResponseEntity<RoleDTO> insertIntoRole(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Nombre del nuevo rol a registrar",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = InsertRoleDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Nuevo Rol (NUEVO)",
                            summary = "Ejemplo de creación de rol desde request.http",
                            value =
                                """
                                {
                                    "name": "NUEVO"
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          InsertRoleDTO insertRoleDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            roleInConverter.toDto(
                newRoleUseCase.newRole(roleInConverter.toDomainInsert(insertRoleDTO))));
  }

  @Operation(
      summary = "Obtener un rol por su ID",
      description =
          "Busca y devuelve los detalles de un rol específico a partir de su identificador. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Rol encontrado",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RoleDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Rol Administrador (ID 1)",
                          summary = "Detalle del rol ADMIN",
                          value =
                              """
                              {
                                "roleId": 1,
                                "name": "ADMIN"
                              }
                              """),
                      @ExampleObject(
                          name = "Rol Usuario Estándar (ID 2)",
                          summary = "Detalle del rol USER",
                          value =
                              """
                              {
                                "roleId": 2,
                                "name": "USER"
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
            description = "No existe ningún rol con el ID proporcionado",
            content = @Content)
      })
  @GetMapping("/{roleId}")
  public ResponseEntity<RoleDTO> getRoleById(
      @Parameter(description = "ID único del rol", example = "2") @PathVariable Integer roleId) {
    return ResponseEntity.ok(roleInConverter.toDto(selectRoleByIdUseCase.selectRoleById(roleId)));
  }

  @Operation(
      summary = "Actualizar un rol",
      description = "Modifica los datos de un rol existente. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Rol actualizado correctamente",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RoleDTO.class),
                    examples = {
                      @ExampleObject(
                          name = "Rol actualizado (ANTIGUO)",
                          summary = "Respuesta tras actualizar el rol 3 a ANTIGUO",
                          value =
                              """
                              {
                                "roleId": 3,
                                "name": "ANTIGUO"
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
            description = "No autorizado (se requiere rol ADMIN, denegado para ROLE_USER)",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ningún rol con el ID proporcionado",
            content = @Content)
      })
  @PutMapping("/{roleId}")
  public ResponseEntity<RoleDTO> updateRole(
      @Parameter(description = "ID único del rol a actualizar", example = "3") @PathVariable
          Integer roleId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Nuevo nombre del rol",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = InsertRoleDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Actualizar Rol (ANTIGUO)",
                            summary = "Ejemplo de actualización de nombre de rol",
                            value =
                                """
                                {
                                    "name": "ANTIGUO"
                                }
                                """)
                      }))
          @RequestBody
          @Valid
          InsertRoleDTO insertRoleDTO) {
    return ResponseEntity.ok(
        roleInConverter.toDto(
            updateRoleUseCase.updateRole(roleId, roleInConverter.toDomainInsert(insertRoleDTO))));
  }

  @Operation(
      summary = "Eliminar un rol",
      description = "Elimina permanentemente un rol por su ID. Requiere rol ADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "Rol eliminado correctamente (sin contenido)",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado (se requiere rol ADMIN, denegado para ROLE_USER)",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "No existe ningún rol con el ID proporcionado",
            content = @Content)
      })
  @DeleteMapping("/{roleId}")
  public ResponseEntity<Void> deleteRole(
      @Parameter(description = "ID único del rol a eliminar", example = "3") @PathVariable
          Integer roleId) {
    deleteRoleUseCase.deleteRole(roleId);
    return ResponseEntity.noContent().build();
  }
}