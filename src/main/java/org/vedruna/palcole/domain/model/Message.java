package org.vedruna.palcole.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
  private Integer messageId;
  private Integer conversationId;
  private Integer senderId;
  private String content;
  private LocalDateTime sentAt = LocalDateTime.now();

  public Message() {}

  public Integer getMessageId() {
    return messageId;
  }

  public void setMessageId(Integer messageId) {
    this.messageId = messageId;
  }

  public Integer getConversationId() {
    return conversationId;
  }

  public void setConversationId(Integer conversationId) {
    this.conversationId = conversationId;
  }

  public Integer getSenderId() {
    return senderId;
  }

  public void setSenderId(Integer senderId) {
    this.senderId = senderId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(LocalDateTime sentAt) {
    this.sentAt = sentAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Message that = (Message) o;
    return Objects.equals(messageId, that.messageId)
        && Objects.equals(conversationId, that.conversationId)
        && Objects.equals(senderId, that.senderId)
        && Objects.equals(content, that.content)
        && Objects.equals(sentAt, that.sentAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, conversationId, senderId, content, sentAt);
  }
}
