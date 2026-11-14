package org.vedruna.palcole.domain.model;

import java.time.LocalDateTime;

public class Messages {
  private int message_id;
  private int conversation_id;
  private int sender_id;
  private String content;
  private LocalDateTime sent_at;

  public Messages() {
    // inicializacion
  }

  public int getMessage_id() {
    return message_id;
  }

  public void setMessage_id(int message_id) {
    this.message_id = message_id;
  }

  public int getConversation_id() {
    return conversation_id;
  }

  public void setConversation_id(int conversation_id) {
    this.conversation_id = conversation_id;
  }

  public int getSender_id() {
    return sender_id;
  }

  public void setSender_id(int sender_id) {
    this.sender_id = sender_id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDateTime getSent_at() {
    return sent_at;
  }

  public void setSent_at(LocalDateTime sent_at) {
    this.sent_at = sent_at;
  }
}
