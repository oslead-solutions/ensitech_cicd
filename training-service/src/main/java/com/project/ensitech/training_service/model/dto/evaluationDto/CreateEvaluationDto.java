package com.project.ensitech.training_service.model.dto.evaluationDto;

import com.project.ensitech.training_service.model.dto.UserDto;
import com.project.ensitech.training_service.model.enumeration.EvaluationStatus;
import com.project.ensitech.training_service.model.enumeration.EvaluationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEvaluationDto {

    @NotBlank(message = "Le code  est obligatoire")
    private String code;
    @NotNull (message = "La date   est obligatoire")
    private LocalDate dateEvaluation;
    @NotNull(message = "La note est obligatoire")
    private Double grade;
    private String description;
    @NotNull(message = "Le type d'évaluation  est obligatoire")
    private EvaluationType type;
    @NotNull(message = "Le status de l'évaluation  est obligatoire")
    private EvaluationStatus status;
    @NotNull(message = "Le cours est obligatoire")
    private Long courseId;
    @NotNull(message = "L'étudiant est obligatoire")
    private Long studentId;
    private UserDto student;
}
