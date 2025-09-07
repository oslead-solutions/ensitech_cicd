package com.project.user_service.service.mapper;

import com.project.user_service.models.dto.StudyManagerDto;
import com.project.user_service.models.entities.StudyManager;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudyManagerMapper {
    StudyManagerDto toDto(StudyManager studyManager);
    StudyManager toEntity(StudyManagerDto studyManagerDto);
}
