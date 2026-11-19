package org.vedruna.palcole.infrastructure.adapters.inbound.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vedruna.palcole.domain.model.Role;
import org.vedruna.palcole.domain.modelutils.PageModel;
import org.vedruna.palcole.domain.modelutils.PageableModel;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.role.InsertRoleDTO;
import org.vedruna.palcole.infrastructure.adapters.inbound.dto.role.RoleDTO;

@Mapper(componentModel = "spring")
public interface RoleInConverter {

  Role toDomain(RoleDTO insertRoleDto);

  @Mapping(target = "roleId", ignore = true)
  Role toDomainInsert(InsertRoleDTO insertRoleDTO);

  RoleDTO toDto(Role role);

  public default PageableModel toPageableModel(Pageable pageable) {
    return new PageableModel(
        pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().toString());
  }

  public default Page<RoleDTO> toRolesPage(PageModel<Role> pageModel, PageableModel pageableModel) {
    // CREAMOS EL PAGEABLE
    PageRequest pr;
    if (pageableModel.getSort().equalsIgnoreCase("UNSORTED")) {
      pr = PageRequest.of(pageModel.getNumber(), pageModel.getSize());
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
    // CREAMOS EL PAGE
    return new PageImpl<>(
        pageModel.getContent().stream().map(this::toDto).toList(),
        pr,
        pageModel.getTotalElements());
  }
}
