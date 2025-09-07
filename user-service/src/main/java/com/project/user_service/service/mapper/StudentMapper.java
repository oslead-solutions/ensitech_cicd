package com.project.user_service.service.mapper;



import com.project.user_service.models.dto.StudentDto;
import com.project.user_service.models.entities.Student;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring") // Indique à MapStruct de générer un bean Spring
public interface StudentMapper {

    // Convertit une entité Student en DTO
    StudentDto toDto(Student student);

    // Convertit un DTO en entité Student
    Student toEntity(StudentDto studentDto);

    // Convertit une liste d'entités en liste de DTOs
    List<StudentDto> toDtoList(List<Student> students);
}