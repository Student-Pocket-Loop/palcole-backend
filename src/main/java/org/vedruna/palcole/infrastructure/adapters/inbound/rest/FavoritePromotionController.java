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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.DeleteFavoritePromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.NewFavoritePromotionUseCase;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.SelectAllFavoritePromotionsUseCase;
import org.vedruna.palcole.domain.ports.inbound.favoritePromotions.SelectFavoritePromotionByIdUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.FavoritePromotionInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.favoritePromotions.FavoritePromotionDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.favoritePromotions.InsertFavoritePromotionDTO;

@CrossOrigin
@AllArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/v1/favorite-promotions")
public class FavoritePromotionController {

  private final NewFavoritePromotionUseCase newFavoritePromotionUseCase;
  private final DeleteFavoritePromotionUseCase deleteFavoritePromotionUseCase;
  private final SelectAllFavoritePromotionsUseCase selectAllFavoritePromotionsUseCase;
  private final SelectFavoritePromotionByIdUseCase selectFavoritePromotionByIdUseCase;
  private final FavoritePromotionInConverter favoritePromotionInConverter;

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @GetMapping("/")
  public ResponseEntity<Page<FavoritePromotionDTO>> getAllFavoritePromotions(Pageable pageable) {
    PageableModel pageableModel = favoritePromotionInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        favoritePromotionInConverter.toFavoritePromotionsPage(
            selectAllFavoritePromotionsUseCase.selectAllFavoritePromotions(pageableModel),
            pageableModel));
  }

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @PostMapping("/")
  public ResponseEntity<FavoritePromotionDTO> insertIntoFavoritePromotion(
      @RequestBody @Valid InsertFavoritePromotionDTO insertFavoritePromotionDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            favoritePromotionInConverter.toDto(
                newFavoritePromotionUseCase.newFavoritePromotion(
                    favoritePromotionInConverter.toDomainInsert(insertFavoritePromotionDTO))));
  }

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @GetMapping("/{userId}/{promotionId}")
  public ResponseEntity<FavoritePromotionDTO> getFavoritePromotionById(
      @PathVariable Integer userId, @PathVariable Integer promotionId) {
    return ResponseEntity.ok(
        favoritePromotionInConverter.toDto(
            selectFavoritePromotionByIdUseCase.selectFavoritePromotionById(userId, promotionId)));
  }

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @DeleteMapping("/{userId}/{promotionId}")
  public ResponseEntity<Void> deleteFavoritePromotion(
      @PathVariable Integer userId, @PathVariable Integer promotionId) {
    deleteFavoritePromotionUseCase.deleteFavoritePromotion(userId, promotionId);
    return ResponseEntity.noContent().build();
  }
}
