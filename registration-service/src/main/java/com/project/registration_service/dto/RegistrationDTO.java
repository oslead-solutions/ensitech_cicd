package com.project.registration_service.dto;

import com.project.registration_service.enumeration.Level;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RegistrationDTO(
        Long registrationNumber,
        Level level
) {
}
