package org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.FavoritePromotionId;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.FavoritePromotionEntity;

@Repository
public interface FavoritePromotionRepository
    extends JpaRepository<FavoritePromotionEntity, FavoritePromotionId> {}
