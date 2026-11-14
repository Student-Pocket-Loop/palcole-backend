package org.vedruna.palcole.domain.model;

import java.time.LocalDateTime;

public class Conversations {
  private int conversation_id;
  private int item_id;
  private int buyer_id;
  private LocalDateTime started_at;

  public Conversations() {
    // inicializacion
  }

  public int getConversation_id() {
    return conversation_id;
  }

  public void setConversation_id(int conversation_id) {
    this.conversation_id = conversation_id;
  }

  public int getItem_id() {
    return item_id;
  }

  public void setItem_id(int item_id) {
    this.item_id = item_id;
  }

  public int getBuyer_id() {
    return buyer_id;
  }

  public void setBuyer_id(int buyer_id) {
    this.buyer_id = buyer_id;
  }

  public LocalDateTime getStarted_at() {
    return started_at;
  }

  public void setStarted_at(LocalDateTime started_at) {
    this.started_at = started_at;
  }
}
