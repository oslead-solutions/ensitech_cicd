package com.project.ensitech.training_service.service.common;

import com.project.ensitech.training_service.model.dto.courseDto.CourseDto;
import com.project.ensitech.training_service.model.dto.courseDto.CreateCourseDto;

import java.util.List;

public interface ICourseService {
    CourseDto createCourse(CreateCourseDto dto);
    CourseDto getCourse(Long id);
    CourseDto updateCourse(CourseDto dto);
    void deleteCourse(Long id);
    List<CourseDto> searchByTitle(String title);
    List<CourseDto> getAllCourses();
    List<CourseDto> getAllCoursesByTeacher(Long id);
}
