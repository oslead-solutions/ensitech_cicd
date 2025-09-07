package com.project.user_service.controller;


import com.project.user_service.models.dto.StudentDto;
import com.project.user_service.service.common.IStudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students") // Endpoint de base pour les étudiants
@RequiredArgsConstructor
public class StudentController {

    private final IStudentService studentService;

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentDto studentDto) {
        StudentDto createdStudent = studentService.createStudent(studentDto);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @RequestBody StudentDto studentDto) {
        return ResponseEntity.ok(studentService.updateStudent(id, studentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {

        studentService.deleteStudent(id);
        // On retourne une réponse vide avec un statut 204 (No Content),
        // ce qui est la norme pour une suppression réussie.
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<StudentDto>> getStudentsByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(studentService.getStudentsByIds(ids));
    }

    @GetMapping("/by-matricule/{matricule}")
    public ResponseEntity<StudentDto> getStudentsByMatricule(@PathVariable String matricule){
        return ResponseEntity.ok(studentService.getStudentByMatricule(matricule));
    }
}


