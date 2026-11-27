package org.vedruna.palcole.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.vedruna.palcole.domain.exception.FavoriteItemAlreadyExistsException;
import org.vedruna.palcole.domain.exception.FavoriteItemNotFoundException;
import org.vedruna.palcole.domain.model.FavoriteItem;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.FavoriteItemStorage;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.FavoriteItemOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.FavoriteItemEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.FavoriteItemId;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.FavoriteItemRepository;

@Component
@AllArgsConstructor
public class FavoriteItemStorageImpl implements FavoriteItemStorage {

  private final FavoriteItemRepository favoriteItemRepository;
  private final FavoriteItemOutConverter favoriteItemOutConverter;

  @Override
  public PageModel<FavoriteItem> selectAllFavoriteItems(PageableModel pageableModel) {
    Pageable pageable = favoriteItemOutConverter.toPageable(pageableModel);
    Page<FavoriteItemEntity> favoriteItemPage = favoriteItemRepository.findAll(pageable);
    return favoriteItemOutConverter.toFavoriteItemPageModel(favoriteItemPage);
  }

  @Override
  public FavoriteItem findFavoriteItemById(Integer userId, Integer itemId) {
    FavoriteItemEntity favoriteItemEntity =
        favoriteItemRepository
            .findById(new FavoriteItemId(userId, itemId))
            .orElseThrow(() -> new FavoriteItemNotFoundException(userId, itemId));
    return favoriteItemOutConverter.toDomain(favoriteItemEntity);
  }

  @Override
  public FavoriteItem insertFavoriteItem(FavoriteItem newFavoriteItem) {
    FavoriteItemId favoriteItemId =
        new FavoriteItemId(newFavoriteItem.getUserId(), newFavoriteItem.getItemId());
    if (favoriteItemRepository.existsById(favoriteItemId)) {
      throw new FavoriteItemAlreadyExistsException(
          newFavoriteItem.getUserId(), newFavoriteItem.getItemId());
    }
    return favoriteItemOutConverter.toDomain(
        favoriteItemRepository.save(favoriteItemOutConverter.toEntity(newFavoriteItem)));
  }

  @Override
  public void deleteFavoriteItem(Integer userId, Integer itemId) {
    if (!favoriteItemRepository.existsById(new FavoriteItemId(userId, itemId))) {
      throw new FavoriteItemNotFoundException(userId, itemId);
    }
    favoriteItemRepository.deleteById(new FavoriteItemId(userId, itemId));
  }
}
