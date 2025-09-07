package com.project.registration_service.dto;

import com.project.registration_service.enumeration.AcademicYearStatus;
import com.project.registration_service.enumeration.Level;
import com.project.registration_service.model.AcademicYear;

// pour la methode post
public record CreateRegistrationDTO(
//        Long registrationNumber,
        String matricule,
        Level level,
        String specialityLabel,
        String academicYearLabel
) {}
