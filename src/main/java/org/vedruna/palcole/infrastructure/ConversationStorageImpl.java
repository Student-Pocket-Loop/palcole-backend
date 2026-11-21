package org.vedruna.palcole.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.vedruna.palcole.domain.exception.conversations.ConversationNotFoundException;
import org.vedruna.palcole.domain.model.Conversation;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.ConversationStorage;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.ConversationOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.ConversationEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.ConversationRepository;

@Component
@AllArgsConstructor
public class ConversationStorageImpl implements ConversationStorage {

  private final ConversationRepository conversationRepository;
  private final ConversationOutConverter conversationOutConverter;

  @Override
  public PageModel<Conversation> selectAllConversations(PageableModel pageableModel) {
    Pageable pageable = conversationOutConverter.toPageable(pageableModel);
    Page<ConversationEntity> conversationPage = conversationRepository.findAll(pageable);
    return conversationOutConverter.toConversationPageModel(conversationPage);
  }

  @Override
  public Conversation findConversationById(Integer conversationId) {
    ConversationEntity conversationEntity =
        conversationRepository
            .findById(conversationId)
            .orElseThrow(() -> new ConversationNotFoundException(conversationId));
    return conversationOutConverter.toDomain(conversationEntity);
  }

  @Override
  public Conversation insertConversation(Conversation newConversation) {
    return conversationOutConverter.toDomain(
        conversationRepository.save(conversationOutConverter.toEntity(newConversation)));
  }

  @Override
  public void deleteConversation(Integer conversationId) {
    if (!conversationRepository.existsById(conversationId)) {
      throw new ConversationNotFoundException(conversationId);
    }
    conversationRepository.deleteById(conversationId);
  }
}
