package com.project.ensitech.training_service.service.implementation;

import com.project.ensitech.training_service.client.StudentClient;
import com.project.ensitech.training_service.model.dto.UserDto;
import com.project.ensitech.training_service.model.dto.courseDto.CourseDto;
import com.project.ensitech.training_service.model.dto.evaluationDto.CreateEvaluationDto;
import com.project.ensitech.training_service.model.dto.evaluationDto.EvaluationDto;
import com.project.ensitech.training_service.model.dto.specialityDto.SpecialityDto;
import com.project.ensitech.training_service.model.entity.Course;
import com.project.ensitech.training_service.model.entity.Evaluation;
import com.project.ensitech.training_service.repository.CourseRepository;
import com.project.ensitech.training_service.repository.EvaluationRepository;
import com.project.ensitech.training_service.service.common.IEvaluationService;
import com.project.ensitech.training_service.service.mapper.EvaluationMapper;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EvaluationServiceImpl implements IEvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final CourseRepository courseRepository;
    private final EvaluationMapper evaluationMapper;
    private final StudentClient studentClient;


    @Override
    public EvaluationDto createEvaluation(CreateEvaluationDto request) {
        // Evaluation evaluation = new Evaluation();

        // Check if course exists
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + request.getCourseId()));

        assertStudentExists(request.getStudentId());

        /*evaluation.setCode(request.getCode());
        evaluation.setDateEvaluation(request.getDateEvaluation());
        evaluation.setGrade(request.getGrade());
        evaluation.setDescription(request.getDescription());
        evaluation.setType(request.getType());
        evaluation.setStatus(request.getStatus());
        evaluation.setCourse(course);
        evaluation.setStudentId(request.getStudentId());*/
        Evaluation evaluation = evaluationMapper.toEntity(request);
        evaluation.setCourse(course);
        Evaluation saved = evaluationRepository.save(evaluation);
        UserDto student = studentClient.getStudent(saved.getStudentId());
        saved.setStudent(student);
        return evaluationMapper.toDto(saved);
    }

    @Override
    public EvaluationDto getEvaluation(Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evaluation not found: " + id));
       //  return  evaluationMapper.toDto(e);

        EvaluationDto evaluationDto = evaluationMapper.toDto(evaluation);

        UserDto student = studentClient.getStudent(evaluationDto.getStudentId());
        evaluationDto.setStudent(student);
        return evaluationDto;
    }

    @Override
    public List<EvaluationDto> getAllEvaluationsByStudentId(Long studentId) {
        return evaluationRepository.findByStudentId(studentId).stream()
                .map(evaluationMapper::toDto)
                .toList();
    }

    @Override
    public List<EvaluationDto> getAllEvaluationsByCourseId(Long courseId) {
        return evaluationRepository.findByCourseId(courseId).stream()
                .map(evaluationMapper::toDto)
                .toList();
    }

    @Override
    public EvaluationDto updateEvaluation(EvaluationDto request) {
        Evaluation existing = evaluationRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Evaluation not found: " + request.getId()));

        // Course course = courseRepository.findById(request.getCourseId())
        Course course = courseRepository.findById(request.getCourse().getId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + request.getCourse().getId()));
               // .orElseThrow(() -> new EntityNotFoundException("Course not found: " + request.getCourseId()));
        assertStudentExists(request.getStudentId());
        existing.setCode(request.getCode());
        existing.setDateEvaluation(request.getDateEvaluation());
        existing.setGrade(request.getGrade());
        existing.setDescription(request.getDescription());
        existing.setType(request.getType());
        existing.setStatus(request.getStatus());
        existing.setCourse(course);
        existing.setStudentId(request.getStudentId());

        Evaluation updated = evaluationRepository.save(existing);
        UserDto student = studentClient.getStudent(updated.getStudentId());
        updated.setStudent(student);
        return evaluationMapper.toDto(updated);
    }

    @Override
    public void deleteEvaluation(Long id) {
        if (!evaluationRepository.existsById(id)) {
            throw new EntityNotFoundException("Evaluation not found: " + id);
        }
        evaluationRepository.deleteById(id);
    }

    @Override
    public List<EvaluationDto> getAllEvaluations() {
       /* return evaluationRepository.findAll()
                .stream()
                .map(evaluationMapper::toDto)
                .toList();*/

        return evaluationRepository.findAll().stream()
                .map(course -> {
                    EvaluationDto dto = evaluationMapper.toDto(course);

                    // Fetch teacher details from User Service
                    UserDto student = studentClient.getStudent(dto.getStudentId());
                    dto.setStudent(student);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    /*private EvaluationDto mapToDto(Evaluation e) {
        return new EvaluationDto(
                e.getId(),
                e.getCode(),
                e.getDate(),
                e.getGrade(),
                e.getDescription(),
                e.getType(),
                e.getStatus(),
                e.getCourse().getId(),
                e.getStudentId()
        );
    }*/

    private void assertStudentExists(Long id) {
        try { studentClient.getStudent(id); }
        catch (FeignException.NotFound e) { throw new EntityNotFoundException("Student "+id+" not found"); }
        // catch (FeignException.NotFound e) { throw new IllegalArgumentException("Student "+id+" not found"); }
    }
}

