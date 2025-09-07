package com.project.user_service.service.common;

import com.project.user_service.models.dto.StudentDto;

import java.util.List;

public interface IStudentService {
    StudentDto createStudent(StudentDto dto);
    List<StudentDto> getAllStudents();
    StudentDto getStudentById(Long id);
    StudentDto updateStudent(Long id, StudentDto dto);
    void deleteStudent(Long id);
    List<StudentDto> getStudentsByIds(List<Long> ids);
    StudentDto getStudentByMatricule(String matricule);
}
