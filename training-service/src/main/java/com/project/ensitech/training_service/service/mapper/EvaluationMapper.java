package com.project.ensitech.training_service.service.mapper;

import com.project.ensitech.training_service.model.dto.evaluationDto.EvaluationDto;
import com.project.ensitech.training_service.model.dto.evaluationDto.CreateEvaluationDto;
import com.project.ensitech.training_service.model.entity.Evaluation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EvaluationMapper {
    Evaluation toEntity(EvaluationDto dto);
    Evaluation toEntity(CreateEvaluationDto dto);
    EvaluationDto toDto(Evaluation entity);
}

