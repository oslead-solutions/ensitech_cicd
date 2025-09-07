package com.project.ensitech.training_service.controller;

import com.project.ensitech.training_service.model.dto.evaluationDto.CreateEvaluationDto;
import com.project.ensitech.training_service.model.dto.evaluationDto.EvaluationDto;
import com.project.ensitech.training_service.service.common.IEvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training/evaluations")
@RequiredArgsConstructor
@ControllerAdvice
public class EvaluationController {
    // private static final Logger log = LoggerFactory.getLogger(EvaluationController.class);
    private final IEvaluationService iEvaluationService;

   

    @GetMapping
    public ResponseEntity<List<EvaluationDto>> getAllEvaluations() {
        return ResponseEntity.ok(iEvaluationService.getAllEvaluations());
    }
    @GetMapping("/by-student/{idStudent}")
    public ResponseEntity<List<EvaluationDto>> getEvaluationsByStudent(@Valid @PathVariable Long idStudent) {
        return ResponseEntity.ok(iEvaluationService.getAllEvaluationsByStudentId(idStudent));
    }
    @GetMapping("/by-course/{idCourse}")
    public ResponseEntity<List<EvaluationDto>> getEvaluationsByCourse(@Valid @PathVariable Long idCourse) {
        return ResponseEntity.ok(iEvaluationService.getAllEvaluationsByCourseId(idCourse));
    }

    @PostMapping
    public ResponseEntity<EvaluationDto> create(@Valid @RequestBody CreateEvaluationDto createEvaluationDto) {

        EvaluationDto created = iEvaluationService.createEvaluation(createEvaluationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationDto> get(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(iEvaluationService.getEvaluation(id));
    }

    @PutMapping
    public ResponseEntity<EvaluationDto> update( @Valid @RequestBody EvaluationDto evaluationDto) {
        return ResponseEntity.ok(iEvaluationService.updateEvaluation( evaluationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable Long id) {
        iEvaluationService.deleteEvaluation(id);
        return ResponseEntity.noContent().build();
    }

}
