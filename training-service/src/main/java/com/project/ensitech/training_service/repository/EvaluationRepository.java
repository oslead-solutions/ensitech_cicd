package com.project.ensitech.training_service.repository;

import com.project.ensitech.training_service.model.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluationRepository  extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByCourseId(Long courseId);
    List<Evaluation> findByStudentId(Long studentId);
    Optional<Evaluation> findByCode(String code);
}
