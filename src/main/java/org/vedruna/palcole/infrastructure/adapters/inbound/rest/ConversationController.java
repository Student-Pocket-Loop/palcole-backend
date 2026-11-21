package org.vedruna.palcole.infrastructure.adapters.inbound.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.conversations.DeleteConversationUseCase;
import org.vedruna.palcole.domain.ports.inbound.conversations.NewConversationUseCase;
import org.vedruna.palcole.domain.ports.inbound.conversations.SelectAllConversationsUseCase;
import org.vedruna.palcole.domain.ports.inbound.conversations.SelectConversationByIdUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.ConversationInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.conversations.ConversationDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.conversations.InsertConversationDTO;


@CrossOrigin
@AllArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/v1/conversations")
public class ConversationController {

  private final NewConversationUseCase newConversationUseCase;
  private final DeleteConversationUseCase deleteConversationUseCase;
  private final SelectAllConversationsUseCase selectAllConversationsUseCase;
  private final SelectConversationByIdUseCase selectConversationByIdUseCase;
  private final ConversationInConverter conversationInConverter;

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @GetMapping("/")
  public ResponseEntity<Page<ConversationDTO>> getAllConversations(Pageable pageable) {
    PageableModel pageableModel = conversationInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        conversationInConverter.toConversationsPage(
            selectAllConversationsUseCase.selectAllConversations(pageableModel), pageableModel));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/")
  public ResponseEntity<ConversationDTO> insertIntoConversation(
      @RequestBody @Valid InsertConversationDTO insertConversationDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            conversationInConverter.toDto(
                newConversationUseCase.newConversation(
                    conversationInConverter.toDomainInsert(insertConversationDTO))));
  }
  
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @GetMapping("/{conversationId}")
  public ResponseEntity<ConversationDTO> getConversationById(@PathVariable Integer conversationId) {
    return ResponseEntity.ok(
        conversationInConverter.toDto(
            selectConversationByIdUseCase.selectConversationById(conversationId)));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{conversationId}")
  public ResponseEntity<Void> deleteConversation(@PathVariable Integer conversationId) {
    deleteConversationUseCase.deleteConversation(conversationId);
    return ResponseEntity.noContent().build();
  }
}
