package com.project.ensitech.training_service.service.common;

import com.project.ensitech.training_service.model.dto.evaluationDto.CreateEvaluationDto;
import com.project.ensitech.training_service.model.dto.evaluationDto.EvaluationDto;

import java.util.List;

public interface IEvaluationService {
    EvaluationDto createEvaluation(CreateEvaluationDto dto);
    EvaluationDto getEvaluation(Long id);
    EvaluationDto updateEvaluation(EvaluationDto dto);
    void deleteEvaluation(Long id);
    List<EvaluationDto> getAllEvaluations();
    List<EvaluationDto> getAllEvaluationsByStudentId(Long studentId);
    List<EvaluationDto> getAllEvaluationsByCourseId(Long courseId);
}
