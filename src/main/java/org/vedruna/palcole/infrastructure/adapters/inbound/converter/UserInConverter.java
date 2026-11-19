package org.vedruna.palcole.infrastructure.adapters.inbound.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vedruna.palcole.domain.model.User;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.user.InsertUserDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.user.UserDTO;

@Mapper(componentModel = "spring")
public interface UserInConverter {

  User toDomain(UserDTO insertUserDto);

  @Mapping(target = "userId", ignore = true)
  @Mapping(target = "rolesRoleId", ignore = true)
  @Mapping(target = "educationalCenter", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  @Mapping(target = "accountExpirationDate", ignore = true)
  @Mapping(target = "isLocked", ignore = true)
  @Mapping(target = "credentialsExpirationDate", ignore = true)
  @Mapping(target = "isEnabled", ignore = true)
  User toDomainInsert(InsertUserDTO insertUserDto);

  UserDTO toDto(User user);

  public default PageableModel toPageableModel(Pageable pageable) {
    return new PageableModel(
        pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().toString());
  }

  public default Page<UserDTO> toUsersPage(PageModel<User> pageModel, PageableModel pageableModel) {
    PageRequest pr;
    if (pageableModel.getSort().equalsIgnoreCase("UNSORTED")) {
      pr = PageRequest.of(pageableModel.getPage(), pageableModel.getSize());
    } else {
      String[] sort = pageableModel.getSort().split(": ");
      pr =
          PageRequest.of(
              pageableModel.getPage(),
              pageableModel.getSize(),
              sort[1].equalsIgnoreCase("ASC")
                  ? Sort.by(sort[0]).ascending()
                  : Sort.by(sort[0]).descending());
    }

    return new PageImpl<>(
        pageModel.getContent().stream().map(this::toDto).toList(),
        pr,
        pageModel.getTotalElements());
  }
}
