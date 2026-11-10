package org.vedruna.palcole.controller.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.vedruna.palcole.controller.dto.UserDto;
import org.vedruna.palcole.controller.dto.authdto.LoginRequestDto;
import org.vedruna.palcole.controller.dto.authdto.RegisterRequestDto;
import org.vedruna.palcole.persistance.model.UserEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter {

  /** Mapea un UserEntity a UserDto extrayendo el nombre del rol desde la relación. */
  @Mapping(source = "userRol.rolName", target = "userRolName")
  UserDto toDto(UserEntity user);

  /** Convierte las credenciales de LoginRequestDto a un objeto UserEntity. */
  UserEntity loginToEntity(LoginRequestDto request);

  /** Convierte los datos de registro de RegisterRequestDto a un objeto UserEntity. */
  UserEntity registerToEntity(RegisterRequestDto request);
}
