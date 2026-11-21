package org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "conversations")
public class ConversationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "conversation_id")
  Integer conversationId;

  @Column(name = "item_id", nullable = false)
  Integer itemId;

  @Column(name = "buyer_id", nullable = false)
  Integer buyerId;

  @Column(name = "started_at")
  LocalDateTime startedAt;
}
