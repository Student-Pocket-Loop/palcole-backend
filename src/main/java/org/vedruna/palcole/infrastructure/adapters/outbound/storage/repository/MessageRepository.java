package org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.MessageEntity;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {}
