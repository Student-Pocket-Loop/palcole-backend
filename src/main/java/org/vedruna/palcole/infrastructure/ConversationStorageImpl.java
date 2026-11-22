package org.vedruna.palcole.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.vedruna.palcole.domain.exception.academicItems.AcademicItemNotFoundException;
import org.vedruna.palcole.domain.exception.conversations.ConversationNotFoundException;
import org.vedruna.palcole.domain.exception.users.UserNotFoundException;
import org.vedruna.palcole.domain.model.Conversation;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.outbound.ConversationStorage;
import org.vedruna.palcole.infrastructure.adapters.outbound.converter.ConversationOutConverter;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.AcademicItemEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.ConversationEntity;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.AcademicItemRepository;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.repository.ConversationRepository;
import org.vedruna.palcole.persistance.model.UserEntity;
import org.vedruna.palcole.persistance.repository.UserRepository;

@Component
@AllArgsConstructor
public class ConversationStorageImpl implements ConversationStorage {

  private final ConversationRepository conversationRepository;
  private final ConversationOutConverter conversationOutConverter;
  private final UserRepository userRepository;
  private final AcademicItemRepository academicItemRepository;

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
    ConversationEntity conversationEntity = conversationOutConverter.toEntity(newConversation);

    Integer itemId = newConversation.getItemId();
    AcademicItemEntity academicItemEntity =
        academicItemRepository
            .findById(itemId)
            .orElseThrow(() -> new AcademicItemNotFoundException(itemId));

    Integer buyerId = newConversation.getBuyerId();
    UserEntity buyerEntity =
        userRepository.findById(buyerId).orElseThrow(() -> new UserNotFoundException(buyerId));

    conversationEntity.setItem(academicItemEntity);
    conversationEntity.setBuyer(buyerEntity);

    return conversationOutConverter.toDomain(conversationRepository.save(conversationEntity));
  }

  @Override
  public void deleteConversation(Integer conversationId) {
    if (!conversationRepository.existsById(conversationId)) {
      throw new ConversationNotFoundException(conversationId);
    }
    conversationRepository.deleteById(conversationId);
  }
}
