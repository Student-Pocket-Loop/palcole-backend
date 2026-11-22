package org.vedruna.palcole.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.vedruna.palcole.domain.exception.categories.CategoryNotFoundException;
import org.vedruna.palcole.domain.exception.promotions.PromotionNotFoundException;
import org.vedruna.palcole.domain.model.Promotion;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.PromotionStorage;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.PromotionOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.CategoryEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.PromotionEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.CategoryRepository;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.PromotionRepository;

@Component
@AllArgsConstructor
public class PromotionStorageImpl implements PromotionStorage {

  private final PromotionRepository promotionRepository;
  private final PromotionOutConverter promotionOutConverter;
  private final CategoryRepository categoryRepository;

  @Override
  public PageModel<Promotion> selectAllPromotions(PageableModel pageableModel) {
    Pageable pageable = promotionOutConverter.toPageable(pageableModel);
    Page<PromotionEntity> promotionPage = promotionRepository.findAll(pageable);
    return promotionOutConverter.toPromotionPageModel(promotionPage);
  }

  @Override
  public Promotion findPromotionById(Integer promotionId) {
    PromotionEntity promotionEntity =
        promotionRepository
            .findById(promotionId)
            .orElseThrow(() -> new PromotionNotFoundException(promotionId));
    return promotionOutConverter.toDomain(promotionEntity);
  }

  @Override
  public Promotion updatePromotion(Integer promotionId, Promotion newPromotion) {
    PromotionEntity promotionEntity =
        promotionRepository
            .findById(promotionId)
            .orElseThrow(() -> new PromotionNotFoundException(promotionId));

    if (newPromotion.getCategoryId() != null) {
      promotionEntity.setCategory(findCategory(newPromotion.getCategoryId()));
    }
    promotionEntity.setTitle(newPromotion.getTitle());
    promotionEntity.setDescription(newPromotion.getDescription());
    promotionEntity.setOriginalPrice(newPromotion.getOriginalPrice());
    promotionEntity.setDiscountPrice(newPromotion.getDiscountPrice());
    promotionEntity.setDiscountPercentage(newPromotion.getDiscountPercentage());
    promotionEntity.setPromoCode(newPromotion.getPromoCode());
    promotionEntity.setStartDate(newPromotion.getStartDate());
    promotionEntity.setEndDate(newPromotion.getEndDate());

    return promotionOutConverter.toDomain(promotionRepository.save(promotionEntity));
  }

  @Override
  public Promotion insertPromotion(Promotion newPromotion) {
    PromotionEntity promotionEntity = promotionOutConverter.toEntity(newPromotion);
    promotionEntity.setCategory(findCategory(newPromotion.getCategoryId()));
    return promotionOutConverter.toDomain(promotionRepository.save(promotionEntity));
  }

  @Override
  public void deletePromotion(Integer promotionId) {
    if (!promotionRepository.existsById(promotionId)) {
      throw new PromotionNotFoundException(promotionId);
    }
    promotionRepository.deleteById(promotionId);
  }

  private CategoryEntity findCategory(Integer id) {
    return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
  }
}
