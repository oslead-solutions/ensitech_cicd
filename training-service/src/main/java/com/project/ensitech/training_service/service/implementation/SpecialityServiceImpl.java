package com.project.ensitech.training_service.service.implementation;

import com.project.ensitech.training_service.controller.CourseController;
import com.project.ensitech.training_service.exception.ResourceNotFoundException;
import com.project.ensitech.training_service.model.dto.courseDto.CourseDto;
import com.project.ensitech.training_service.model.dto.specialityDto.CreateSpecialityDto;
import com.project.ensitech.training_service.model.dto.specialityDto.SpecialityDto;
import com.project.ensitech.training_service.model.entity.Course;
import com.project.ensitech.training_service.model.entity.Speciality;
import com.project.ensitech.training_service.repository.CourseRepository;
import com.project.ensitech.training_service.repository.SpecialityRepository;
import com.project.ensitech.training_service.service.common.ISpecialityService;
import com.project.ensitech.training_service.service.mapper.SpecialityMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SpecialityServiceImpl implements ISpecialityService {
    private static final Logger log = LoggerFactory.getLogger(SpecialityServiceImpl.class);
    private final SpecialityRepository specialityRepository;
    private final CourseRepository courseRepository;
    private final SpecialityMapper specialityMapper;



    @Override
    public SpecialityDto createSpeciality(CreateSpecialityDto request) {
        Speciality speciality = new Speciality();
        speciality.setLabel(request.getLabel());
        speciality.setDescription(request.getDescription());
        speciality.setCycle(request.getCycle());

        // Fetch courses by ids and set relation (handle empty or missing gracefully)

        //if (request.getCourseIds() != null && !request.getCourseIds().isEmpty()) {
        if (request.getCourses() != null && !request.getCourses().isEmpty()) {
            //var courses = courseRepository.findAllById(request.getCourseIds());
            Set<Long> courseIds = request.getCourses().stream()
                    .map(CourseDto::getId) // or getId() from your DTO
                    .collect(Collectors.toSet());
            var courses = courseRepository.findAllById(courseIds);
            speciality.setCourses(new HashSet<>(courses));
        }

       speciality.getCourses().forEach(course -> log.info("Specialité course", course.getId()));


// Save the entity
        Speciality saved = specialityRepository.save(speciality);
        saved = specialityRepository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Speciality not found"));

// Map entity -> DTO
        return specialityMapper.toDto(saved);
    }

    @Override
    public SpecialityDto getSpeciality(Long id) {
        Speciality spec = specialityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speciality not found: " + id));
        return specialityMapper.toDto(spec);
    }

    @Override
    public List<SpecialityDto> getAllSpecialities() {
        return specialityRepository.findAll()
                .stream()
                .map(specialityMapper::toDto)
                .toList();
    }

    @Override
    public SpecialityDto getSpecialityByLabel(String label) {
        return specialityRepository.findByLabel(label)
                .map(specialityMapper::toDto)
                .orElseThrow(()-> new EntityNotFoundException("Speciality not found with the label: "+ label));
    }

    @Override
    public SpecialityDto updateSpeciality(SpecialityDto request) {
        Speciality existing = specialityRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Speciality not found: " + request.getId()));

        existing.setLabel(request.getLabel());
        existing.setDescription(request.getDescription());
        existing.setCycle(request.getCycle());

        //if (request.getCourseIds() != null) {
        if (request.getCourses() != null) {
          //  var courses = courseRepository.findAllById(request.getCourseIds());
            Set<Long> courseIds = request.getCourses().stream()
                    .map(CourseDto::getId) // or getId() from your DTO
                    .collect(Collectors.toSet());
            var courses = courseRepository.findAllById(courseIds);
            existing.setCourses(new HashSet<>(courses));
        }

        Speciality updated = specialityRepository.save(existing);
        return specialityMapper.toDto(updated);
    }

    @Override
    public void deleteSpeciality(Long id) {
        if (!specialityRepository.existsById(id)) {
            throw new EntityNotFoundException("Speciality not found: " + id);
        }
        specialityRepository.deleteById(id);
    }
    @Override
    public List<SpecialityDto> searchByLabel(String label) {


        return specialityRepository.findByLabelContainingIgnoreCase(label)
                .stream()
                .map(specialityMapper::toDto)
                .collect(Collectors.toList());
    }

    // Mapping from entity to DTO to avoid exposing JPA entities externally
   /* private SpecialityDto mapToDto(Speciality s) {
        Set<Long> courseIds = s.getCourses().stream()
                .map(Course::getId)
                .collect(Collectors.toSet());

        return new SpecialityDto(
                s.getId(),
                s.getLabel(),
                s.getDescription(),
                s.getCycle(),
                courseIds
        );
    }*/
}
