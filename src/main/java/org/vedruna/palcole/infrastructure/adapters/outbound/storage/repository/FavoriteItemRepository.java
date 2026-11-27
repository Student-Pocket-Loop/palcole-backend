package org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.FavoriteItemEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.FavoriteItemId;

public interface FavoriteItemRepository extends JpaRepository<FavoriteItemEntity, FavoriteItemId> {}
