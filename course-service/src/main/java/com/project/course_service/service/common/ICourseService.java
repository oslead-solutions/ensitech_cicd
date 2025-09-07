package com.project.course_service.service.common;


import com.project.course_service.entities.dto.CourseDto;

import java.util.List;

public interface ICourseService {
    CourseDto createCourse(CourseDto dto);
    CourseDto updateCourse(Long courseId, CourseDto dto);
    CourseDto getCourseById(Long id);
    List<CourseDto> getAllCourses();
    void deleteCourse(Long id);

    // Méthodes pour gérer les associations
    CourseDto assignTeacherToCourse(Long courseId, Long teacherId);
    CourseDto enrollStudentInCourse(Long courseId, Long studentId);
    CourseDto removeStudentFromCourse(Long courseId, Long studentId);
}
