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
import org.vedruna.palcole.domain.ports.inbound.academicItems.DeleteAcademicItemUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.NewAcademicItemUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.SelectAcademicItemByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.SelectAllAcademicItemsUseCase;
import org.vedruna.palcole.domain.ports.inbound.academicItems.UpdateAcademicItemUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.AcademicItemInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems.AcademicItemDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems.InsertAcademicItemDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.academicItems.UpdateAcademicItemDTO;


@CrossOrigin
@AllArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/v1/academic-items")
public class AcademicItemController {

  private final NewAcademicItemUseCase newAcademicItemUseCase;
  private final DeleteAcademicItemUseCase deleteAcademicItemUseCase;
  private final SelectAllAcademicItemsUseCase selectAllAcademicItemsUseCase;
  private final SelectAcademicItemByIdUseCase selectAcademicItemByIdUseCase;
  private final UpdateAcademicItemUseCase updateAcademicItemUseCase;
  private final AcademicItemInConverter academicItemInConverter;

  @GetMapping("/")
  public ResponseEntity<Page<AcademicItemDTO>> getAllAcademicItems(Pageable pageable) {
    PageableModel pageableModel = academicItemInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        academicItemInConverter.toAcademicItemsPage(
            selectAllAcademicItemsUseCase.selectAllAcademicItems(pageableModel), pageableModel));
  }

  @PreAuthorize("hasRole('ADMIN') or #insertAcademicItemDTO.studentId == principal.userId")
  @PostMapping("/")
  public ResponseEntity<AcademicItemDTO> insertIntoAcademicItem(
      @RequestBody @Valid InsertAcademicItemDTO insertAcademicItemDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            academicItemInConverter.toDto(
                newAcademicItemUseCase.newAcademicItem(
                    academicItemInConverter.toDomainInsert(insertAcademicItemDTO))));
  }

  @GetMapping("/{itemId}")
  public ResponseEntity<AcademicItemDTO> getAcademicItemById(@PathVariable Integer itemId) {
    return ResponseEntity.ok(
        academicItemInConverter.toDto(
            selectAcademicItemByIdUseCase.selectAcademicItemById(itemId)));
  }

  @PutMapping("/{itemId}")
  public ResponseEntity<AcademicItemDTO> updateAcademicItem(
      @PathVariable Integer itemId,
      @RequestBody @Valid UpdateAcademicItemDTO updateAcademicItemDTO) {
    return ResponseEntity.ok(
        academicItemInConverter.toDto(
            updateAcademicItemUseCase.updateAcademicItem(
                itemId, academicItemInConverter.toDomainUpdate(updateAcademicItemDTO))));
  }

  @DeleteMapping("/{itemId}")
  public ResponseEntity<Void> deleteAcademicItem(@PathVariable Integer itemId) {
    deleteAcademicItemUseCase.deleteAcademicItem(itemId);
    return ResponseEntity.noContent().build();
  }
}
