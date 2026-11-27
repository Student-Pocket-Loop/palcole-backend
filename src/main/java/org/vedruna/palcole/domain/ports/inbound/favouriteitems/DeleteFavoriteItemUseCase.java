package org.vedruna.palcole.domain.ports.inbound.favouriteitems;

public interface DeleteFavoriteItemUseCase {
  void deleteFavoriteItem(Integer userId, Integer itemId);
}
