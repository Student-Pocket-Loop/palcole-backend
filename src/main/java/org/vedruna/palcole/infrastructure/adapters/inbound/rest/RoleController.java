package org.vedruna.palcole.infrastructure.adapters.inbound.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
import org.vedruna.palcole.domain.ports.inbound.DeleteRoleUseCase;
import org.vedruna.palcole.domain.ports.inbound.NewRoleUseCase;
import org.vedruna.palcole.domain.ports.inbound.SelectAllRolesUseCase;
import org.vedruna.palcole.domain.ports.inbound.SelectRoleByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.UpdateRoleUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.RoleInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.InsertRoleDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.RoleDTO;

@CrossOrigin
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

  private final NewRoleUseCase newRoleUseCase;
  private final DeleteRoleUseCase deleteRoleUseCase;
  private final SelectAllRolesUseCase selectAllRolesUseCase;
  private final SelectRoleByIdUseCase selectRoleByIdUseCase;
  private final UpdateRoleUseCase updateRoleUseCase;
  private final RoleInConverter roleInConverter;

  @GetMapping("/")
  public ResponseEntity<Page<RoleDTO>> getAllRoles(Pageable pageable) {
    PageableModel pageableModel = roleInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        roleInConverter.toRolesPage(
            selectAllRolesUseCase.selectAllRoles(pageableModel), pageableModel));
  }

  @PostMapping("/")
  public ResponseEntity<RoleDTO> insertIntoRole(@RequestBody @Valid InsertRoleDTO insertRoleDTO) {
    return ResponseEntity.ok(
        roleInConverter.toDto(newRoleUseCase.newRole(roleInConverter.toDomainInsert(insertRoleDTO))));
  }

  @GetMapping("/{roleId}")
  public ResponseEntity<RoleDTO> getRoleById(@PathVariable Integer studentId) {
    return ResponseEntity.ok(
        roleInConverter.toDto(selectRoleByIdUseCase.selectRoleById(studentId)));
  }

  @PutMapping("/{roleId}")
  public ResponseEntity<RoleDTO> updateStudent(
      @PathVariable Integer roleId, @RequestBody @Valid InsertRoleDTO updateStudentDto) {
    return ResponseEntity.ok(
        roleInConverter.toDto(
            updateRoleUseCase.updateRole(roleId, roleInConverter.toDomainInsert(updateStudentDto))));
  }

  @DeleteMapping("/{roleId}")
  public ResponseEntity<Void> deleteStudent(@PathVariable Integer studentId) {
    deleteRoleUseCase.deleteRole(studentId);
    return ResponseEntity.noContent().build();
  }
}
