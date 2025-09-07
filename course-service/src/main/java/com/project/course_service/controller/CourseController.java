package com.project.course_service.controller;

import com.project.course_service.entities.dto.CourseDto;
import com.project.course_service.service.common.ICourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200") // On autorise explicitement l'origine de votre front-end Angular
public class CourseController {
    private final ICourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto dto) {
        return new ResponseEntity<>(courseService.createCourse(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDto dto) {
        return ResponseEntity.ok(courseService.updateCourse(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    // --- Endpoints d'association ---

    @PutMapping("/{courseId}/teacher/{teacherId}")
    public ResponseEntity<CourseDto> assignTeacherToCourse(@PathVariable Long courseId, @PathVariable Long teacherId) {
        return ResponseEntity.ok(courseService.assignTeacherToCourse(courseId, teacherId));
    }

    @PutMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<CourseDto> enrollStudentInCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        return ResponseEntity.ok(courseService.enrollStudentInCourse(courseId, studentId));
    }

    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<CourseDto> removeStudentFromCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        return ResponseEntity.ok(courseService.removeStudentFromCourse(courseId, studentId));
    }
}
