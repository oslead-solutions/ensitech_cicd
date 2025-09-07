package com.project.registration_service.dto;

import com.project.registration_service.enumeration.Level;
import com.project.registration_service.model.Student;

import java.time.LocalDate;

public record RegDTO(
        Long registrationNumber,
        Level level, // Enum Level sous forme de String
        String matricule,
        String specialityLabel,
        String academicYearLabel,
        Student student
) {}
