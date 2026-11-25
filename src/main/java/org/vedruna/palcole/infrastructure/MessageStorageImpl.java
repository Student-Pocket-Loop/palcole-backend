package org.vedruna.palcole.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.vedruna.palcole.domain.exception.messages.MessageNotFoundException;
import org.vedruna.palcole.domain.model.Message;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.MessageStorage;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.MessageOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.MessageEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.MessageRepository;

@Component
@AllArgsConstructor
public class MessageStorageImpl implements MessageStorage {

  private final MessageRepository messageRepository;
  private final MessageOutConverter messageOutConverter;

  @Override
  public PageModel<Message> selectAllMessages(PageableModel pageableModel) {
    Pageable pageable = messageOutConverter.toPageable(pageableModel);
    Page<MessageEntity> messagePage = messageRepository.findAll(pageable);
    return messageOutConverter.toMessagePageModel(messagePage);
  }

  @Override
  public Message findMessageById(Integer messageId) {
    MessageEntity messageEntity =
        messageRepository
            .findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException(messageId));
    return messageOutConverter.toDomain(messageEntity);
  }

  @Override
  public Message updateMessage(Integer messageId, Message newMessage) {
    MessageEntity messageEntity =
        messageRepository
            .findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException(messageId));

    messageEntity.setContent(newMessage.getContent());

    return messageOutConverter.toDomain(messageRepository.save(messageEntity));
  }

  @Override
  public Message insertMessage(Message newMessage) {
    return messageOutConverter.toDomain(
        messageRepository.save(messageOutConverter.toEntity(newMessage)));
  }

  @Override
  public void deleteMessage(Integer messageId) {
    if (!messageRepository.existsById(messageId)) {
      throw new MessageNotFoundException(messageId);
    }
    messageRepository.deleteById(messageId);
  }
}
