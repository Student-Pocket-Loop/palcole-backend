package org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import org.vedruna.palcole.persistance.model.UserEntity;

@Data
@Entity
@Table(name = "messages")
public class MessageEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "message_id")
  Integer messageId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "conversation_id", referencedColumnName = "conversation_id", nullable = false)
  ConversationEntity conversation;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "sender_id", referencedColumnName = "user_id", nullable = false)
  UserEntity sender;

  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  String content;

  @Column(name = "sent_at")
  LocalDateTime sentAt;
}
