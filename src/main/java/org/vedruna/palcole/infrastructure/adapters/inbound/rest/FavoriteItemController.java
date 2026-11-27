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
import org.vedruna.palcole.domain.ports.inbound.favouriteitems.DeleteFavoriteItemUseCase;
import org.vedruna.palcole.domain.ports.inbound.favouriteitems.NewFavoriteItemUseCase;
import org.vedruna.palcole.domain.ports.inbound.favouriteitems.SelectAllFavoriteItemsUseCase;
import org.vedruna.palcole.domain.ports.inbound.favouriteitems.SelectFavoriteItemByIdUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.FavoriteItemInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.favouriteitem.FavoriteItemDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.favouriteitem.InsertFavoriteItemDTO;

@CrossOrigin
@AllArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/v1/favorite-items")
public class FavoriteItemController {

  private final NewFavoriteItemUseCase newFavoriteItemUseCase;
  private final DeleteFavoriteItemUseCase deleteFavoriteItemUseCase;
  private final SelectAllFavoriteItemsUseCase selectAllFavoriteItemsUseCase;
  private final SelectFavoriteItemByIdUseCase selectFavoriteItemByIdUseCase;
  private final FavoriteItemInConverter favoriteItemInConverter;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/")
  public ResponseEntity<Page<FavoriteItemDTO>> getAllFavoriteItems(Pageable pageable) {
    PageableModel pageableModel = favoriteItemInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        favoriteItemInConverter.toFavoriteItemsPage(
            selectAllFavoriteItemsUseCase.selectAllFavoriteItems(pageableModel), pageableModel));
  }

  @PreAuthorize("hasRole('ADMIN') or #insertFavoriteItemDTO.userId == principal.userId")
  @PostMapping("/")
  public ResponseEntity<FavoriteItemDTO> insertIntoFavoriteItem(
      @RequestBody @Valid InsertFavoriteItemDTO insertFavoriteItemDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            favoriteItemInConverter.toDto(
                newFavoriteItemUseCase.newFavoriteItem(
                    favoriteItemInConverter.toDomainInsert(insertFavoriteItemDTO))));
  }

  @PreAuthorize("hasRole('ADMIN') or #userId == principal.userId")
  @GetMapping("/{userId}/{itemId}")
  public ResponseEntity<FavoriteItemDTO> getFavoriteItemById(
      @PathVariable Integer userId, @PathVariable Integer itemId) {
    return ResponseEntity.ok(
        favoriteItemInConverter.toDto(
            selectFavoriteItemByIdUseCase.selectFavoriteItemById(userId, itemId)));
  }

  @PreAuthorize("hasRole('ADMIN') or #userId == principal.userId")
  @DeleteMapping("/{userId}/{itemId}")
  public ResponseEntity<Void> deleteFavoriteItem(
      @PathVariable Integer userId, @PathVariable Integer itemId) {
    deleteFavoriteItemUseCase.deleteFavoriteItem(userId, itemId);
    return ResponseEntity.noContent().build();
  }
}
