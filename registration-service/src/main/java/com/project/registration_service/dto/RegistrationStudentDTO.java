package com.project.registration_service.dto;

import com.project.registration_service.model.Student;
import lombok.Builder;

import java.time.LocalDate;
@Builder
public record RegistrationStudentDTO(
        Long id,
        String registrationNumber,
        String level, // Enum Level sous forme de String
        LocalDate dateOfRegistration,
        String matricule,
        Student student
) {
}




