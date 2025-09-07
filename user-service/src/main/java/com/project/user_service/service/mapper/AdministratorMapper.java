package com.project.user_service.service.mapper;

import com.project.user_service.models.dto.AdministratorDto;
import com.project.user_service.models.entities.Administrator;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")//indique à MapStruct de créer un bean spring
public interface AdministratorMapper {
    AdministratorDto toDto(Administrator administrator);
    Administrator toEntity(AdministratorDto administratorDto);
}
