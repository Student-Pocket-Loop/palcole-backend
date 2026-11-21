package org.vedruna.palcole.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.vedruna.palcole.domain.exception.favoritePromotions.FavoritePromotionAlreadyExistsException;
import org.vedruna.palcole.domain.exception.favoritePromotions.FavoritePromotionNotFoundException;
import org.vedruna.palcole.domain.model.FavoritePromotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.FavoritePromotionStorage;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.FavoritePromotionOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.FavoritePromotionId;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.FavoritePromotionEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.FavoritePromotionRepository;


@Component
@AllArgsConstructor
public class FavoritePromotionStorageImpl implements FavoritePromotionStorage {

  private final FavoritePromotionRepository favoritePromotionRepository;
  private final FavoritePromotionOutConverter favoritePromotionOutConverter;

  @Override
  public PageModel<FavoritePromotion> selectAllFavoritePromotions(PageableModel pageableModel) {
    Pageable pageable = favoritePromotionOutConverter.toPageable(pageableModel);
    Page<FavoritePromotionEntity> favoritePromotionPage =
        favoritePromotionRepository.findAll(pageable);
    return favoritePromotionOutConverter.toFavoritePromotionPageModel(favoritePromotionPage);
  }

  @Override
  public FavoritePromotion findFavoritePromotionById(Integer userId, Integer promotionId) {
    FavoritePromotionEntity favoritePromotionEntity =
        favoritePromotionRepository
            .findById(new FavoritePromotionId(userId, promotionId))
            .orElseThrow(() -> new FavoritePromotionNotFoundException(userId, promotionId));
    return favoritePromotionOutConverter.toDomain(favoritePromotionEntity);
  }

  @Override
  public FavoritePromotion insertFavoritePromotion(FavoritePromotion newFavoritePromotion) {
    FavoritePromotionId favoritePromotionId =
        new FavoritePromotionId(
            newFavoritePromotion.getUserId(), newFavoritePromotion.getPromotionId());
    if (favoritePromotionRepository.existsById(favoritePromotionId)) {
      throw new FavoritePromotionAlreadyExistsException(
          newFavoritePromotion.getUserId(), newFavoritePromotion.getPromotionId());
    }
    return favoritePromotionOutConverter.toDomain(
        favoritePromotionRepository.save(
            favoritePromotionOutConverter.toEntity(newFavoritePromotion)));
  }

  @Override
  public void deleteFavoritePromotion(Integer userId, Integer promotionId) {
    if (!favoritePromotionRepository.existsById(new FavoritePromotionId(userId, promotionId))) {
      throw new FavoritePromotionNotFoundException(userId, promotionId);
    }
    favoritePromotionRepository.deleteById(new FavoritePromotionId(userId, promotionId));
  }
}
