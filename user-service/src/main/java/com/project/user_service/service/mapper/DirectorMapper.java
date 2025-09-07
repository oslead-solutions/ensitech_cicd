package com.project.user_service.service.mapper;

import com.project.user_service.models.dto.DirectorDto;
import com.project.user_service.models.entities.Director;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DirectorMapper {
    DirectorDto toDto(Director director);
    Director toEntity(DirectorDto directorDto);
}
