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
@Table(name = "messages")
public class MessageEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "message_id")
  Integer messageId;

  @Column(name = "conversation_id", nullable = false)
  Integer conversationId;

  @Column(name = "sender_id", nullable = false)
  Integer senderId;

  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  String content;

  @Column(name = "sent_at")
  LocalDateTime sentAt;
}
