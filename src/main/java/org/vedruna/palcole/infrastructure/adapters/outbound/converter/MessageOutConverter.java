package org.vedruna.palcole.infrastructure.adapters.outbound.converter;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vedruna.palcole.domain.model.Message;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.outbound.storage.entity.MessageEntity;

@Mapper(componentModel = "spring")
public interface MessageOutConverter {

  Message toDomain(MessageEntity messageEntity);

  MessageEntity toEntity(Message message);

  /** Convierte el modelo de paginación del dominio al objeto Pageable de Spring Data. */
  public default Pageable toPageable(PageableModel pageableModel) {
    if (pageableModel.getSort().equalsIgnoreCase("UNSORTED")) {
      return PageRequest.of(pageableModel.getPage(), pageableModel.getSize());
    }
    String[] sort = pageableModel.getSort().split(": ");
    return PageRequest.of(
        pageableModel.getPage(),
        pageableModel.getSize(),
        sort[1].equalsIgnoreCase("ASC")
            ? Sort.by(sort[0]).ascending()
            : Sort.by(sort[0]).descending());
  }

  /** Convierte una página de entidades en una página de modelos de dominio. */
  public default PageModel<Message> toMessagePageModel(Page<MessageEntity> page) {
    return new PageModel<>(
        page.getContent().stream().map(this::toDomain).toList(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.getNumberOfElements(),
        page.getSize(),
        page.getNumber());
  }
}
