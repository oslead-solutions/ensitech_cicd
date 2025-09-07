package com.project.ensitech.training_service.controller;

import com.project.ensitech.training_service.model.dto.courseDto.CourseDto;
import com.project.ensitech.training_service.model.dto.courseDto.CreateCourseDto;
import com.project.ensitech.training_service.service.common.ICourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training/courses")
@RequiredArgsConstructor
@ControllerAdvice
public class CourseController {
    private static final Logger log = LoggerFactory.getLogger(CourseController.class);
    private final ICourseService iCourseService;

   /* @GetMapping
    public ResponseEntity<List<CourseDto>> getAll() {
        return ResponseEntity.ok(iCourseService.getAllCourses());
    }*/

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(iCourseService.getAllCourses());
    }

    @PostMapping
    public ResponseEntity<CourseDto> create(@Valid @RequestBody CreateCourseDto createCourseDto) {

        CourseDto created = iCourseService.createCourse(createCourseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> get(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(iCourseService.getCourse(id));
    }
    @GetMapping("/by-teacher/{idTeacher}")
    public ResponseEntity<List<CourseDto>> getByTeacher(@Valid @PathVariable Long idTeacher) {
        return ResponseEntity.ok(iCourseService.getAllCoursesByTeacher(idTeacher));
    }

    @PutMapping
    public ResponseEntity<CourseDto> update( @Valid @RequestBody CourseDto courseDto) {
        return ResponseEntity.ok(iCourseService.updateCourse( courseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable Long id) {
        iCourseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<List<CourseDto>> search(@RequestParam  String title) {
        log.info("searchByTitle controller: ",title);
        // iCourseService.searchByTitle(title);
        // return ResponseEntity.noContent().build();
        return ResponseEntity.ok(iCourseService.searchByTitle(title));
    }
}
