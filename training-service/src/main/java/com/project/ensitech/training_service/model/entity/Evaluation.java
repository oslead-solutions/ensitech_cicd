package com.project.ensitech.training_service.model.entity;

import com.project.ensitech.training_service.model.dto.UserDto;
import com.project.ensitech.training_service.model.enumeration.EvaluationType;
import com.project.ensitech.training_service.model.enumeration.EvaluationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "evaluations", indexes = @Index(columnList = "studentId"))
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate dateEvaluation;
    private Double grade;
    @Column(length = 1000)
    private String description;

    @Column(unique = true, nullable = false)
    private String code; // registration number

    @Enumerated(EnumType.STRING)
    private EvaluationType type;

    @Enumerated(EnumType.STRING)
    private EvaluationStatus status;

    // Link to course
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // Student is external service — store studentId
    private Long studentId;
    @Transient
    private UserDto student;


}
