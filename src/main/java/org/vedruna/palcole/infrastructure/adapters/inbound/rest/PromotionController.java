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
import org.vedruna.palcole.domain.ports.inbound.promotions.DeletePromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.NewPromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.SelectAllPromotionsUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.SelectPromotionByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.promotions.UpdatePromotionUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.PromotionInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.promotions.InsertPromotionDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.promotions.PromotionDTO;

@CrossOrigin
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController {

  private final NewPromotionUseCase newPromotionUseCase;
  private final DeletePromotionUseCase deletePromotionUseCase;
  private final SelectAllPromotionsUseCase selectAllPromotionsUseCase;
  private final SelectPromotionByIdUseCase selectPromotionByIdUseCase;
  private final UpdatePromotionUseCase updatePromotionUseCase;
  private final PromotionInConverter promotionInConverter;

  @GetMapping("/")
  public ResponseEntity<Page<PromotionDTO>> getAllPromotions(Pageable pageable) {
    PageableModel pageableModel = promotionInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        promotionInConverter.toPromotionsPage(
            selectAllPromotionsUseCase.selectAllPromotions(pageableModel), pageableModel));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/")
  public ResponseEntity<PromotionDTO> insertIntoPromotion(
      @RequestBody @Valid InsertPromotionDTO insertPromotionDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            promotionInConverter.toDto(
                newPromotionUseCase.newPromotion(
                    promotionInConverter.toDomainInsert(insertPromotionDTO))));
  }

  @GetMapping("/{promotionId}")
  public ResponseEntity<PromotionDTO> getPromotionById(@PathVariable Integer promotionId) {
    return ResponseEntity.ok(
        promotionInConverter.toDto(selectPromotionByIdUseCase.selectPromotionById(promotionId)));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{promotionId}")
  public ResponseEntity<PromotionDTO> updatePromotion(
      @PathVariable Integer promotionId,
      @RequestBody @Valid InsertPromotionDTO insertPromotionDTO) {
    return ResponseEntity.ok(
        promotionInConverter.toDto(
            updatePromotionUseCase.updatePromotion(
                promotionId, promotionInConverter.toDomainInsert(insertPromotionDTO))));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{promotionId}")
  public ResponseEntity<Void> deletePromotion(@PathVariable Integer promotionId) {
    deletePromotionUseCase.deletePromotion(promotionId);
    return ResponseEntity.noContent().build();
  }
}
