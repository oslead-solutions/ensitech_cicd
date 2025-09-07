package com.project.ensitech.training_service;

import com.project.ensitech.training_service.exception.ResourceNotFoundException;
import com.project.ensitech.training_service.model.dto.courseDto.CourseDto;
import com.project.ensitech.training_service.model.dto.courseDto.CreateCourseDto;
import com.project.ensitech.training_service.model.entity.Course;
import com.project.ensitech.training_service.repository.CourseRepository;
import com.project.ensitech.training_service.service.implementation.CourseServiceImpl;
import com.project.ensitech.training_service.service.mapper.CourseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    CourseRepository courseRepository;

    @Mock
    CourseMapper courseMapper;

    @InjectMocks
    CourseServiceImpl service;

    @Test
    void createCourse_shouldSaveAndReturnDto() {
        CreateCourseDto createCourseDto = new CreateCourseDto("Java", 3, 30,null,null);
        Course course = new Course();
        course.setTitle("Java");
        // mapping courseDto -> entity
        when(courseMapper.toEntity(createCourseDto)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toDto(course)).thenReturn(new CourseDto(1L, "Java", 3, 30, null,null));

        CourseDto result = service.createCourse(createCourseDto);

        assertNotNull(result);
        assertEquals("Java", result.getTitle());
        verify(courseRepository).save(course);
    }

    @Test
    void getCourse_whenNotFound_shouldThrow() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getCourse(99L));
    }
}
