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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.domain.ports.inbound.messages.DeleteMessageUseCase;
import org.vedruna.palcole.domain.ports.inbound.messages.NewMessageUseCase;
import org.vedruna.palcole.domain.ports.inbound.messages.SelectAllMessagesUseCase;
import org.vedruna.palcole.domain.ports.inbound.messages.SelectMessageByIdUseCase;
import org.vedruna.palcole.domain.ports.inbound.messages.UpdateMessageUseCase;
import org.vedruna.palcole.infrastructure.adapters.inbound.converter.MessageInConverter;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.messages.InsertMessageDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.messages.MessageDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.messages.UpdateMessageDTO;

@CrossOrigin
@AllArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

  private final NewMessageUseCase newMessageUseCase;
  private final DeleteMessageUseCase deleteMessageUseCase;
  private final SelectAllMessagesUseCase selectAllMessagesUseCase;
  private final SelectMessageByIdUseCase selectMessageByIdUseCase;
  private final UpdateMessageUseCase updateMessageUseCase;
  private final MessageInConverter messageInConverter;

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @GetMapping("/")
  public ResponseEntity<Page<MessageDTO>> getAllMessages(Pageable pageable) {
    PageableModel pageableModel = messageInConverter.toPageableModel(pageable);
    return ResponseEntity.ok(
        messageInConverter.toMessagesPage(
            selectAllMessagesUseCase.selectAllMessages(pageableModel), pageableModel));
  }

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @PostMapping("/")
  public ResponseEntity<MessageDTO> insertIntoMessage(
      @RequestBody @Valid InsertMessageDTO insertMessageDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            messageInConverter.toDto(
                newMessageUseCase.newMessage(messageInConverter.toDomainInsert(insertMessageDTO))));
  }

  @GetMapping("/{messageId}")
  public ResponseEntity<MessageDTO> getMessageById(@PathVariable Integer messageId) {
    return ResponseEntity.ok(
        messageInConverter.toDto(selectMessageByIdUseCase.selectMessageById(messageId)));
  }

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @PutMapping("/{messageId}")
  public ResponseEntity<MessageDTO> updateMessage(
      @PathVariable Integer messageId, @RequestBody @Valid UpdateMessageDTO updateMessageDTO) {
    return ResponseEntity.ok(
        messageInConverter.toDto(
            updateMessageUseCase.updateMessage(
                messageId, messageInConverter.toDomainUpdate(updateMessageDTO))));
  }

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable Integer messageId) {
    deleteMessageUseCase.deleteMessage(messageId);
    return ResponseEntity.noContent().build();
  }
}
