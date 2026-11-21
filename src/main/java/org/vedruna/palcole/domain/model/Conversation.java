package org.vedruna.palcole.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Conversation {
  private Integer conversationId;
  private Integer itemId;
  private Integer buyerId;
  private LocalDateTime startedAt = LocalDateTime.now();

  public Conversation() {}

  public Integer getConversationId() {
    return conversationId;
  }

  public void setConversationId(Integer conversationId) {
    this.conversationId = conversationId;
  }

  public Integer getItemId() {
    return itemId;
  }

  public void setItemId(Integer itemId) {
    this.itemId = itemId;
  }

  public Integer getBuyerId() {
    return buyerId;
  }

  public void setBuyerId(Integer buyerId) {
    this.buyerId = buyerId;
  }

  public LocalDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(LocalDateTime startedAt) {
    this.startedAt = startedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Conversation that = (Conversation) o;
    return Objects.equals(conversationId, that.conversationId)
        && Objects.equals(itemId, that.itemId)
        && Objects.equals(buyerId, that.buyerId)
        && Objects.equals(startedAt, that.startedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(conversationId, itemId, buyerId, startedAt);
  }
  
}
