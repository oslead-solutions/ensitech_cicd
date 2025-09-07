package com.project.registration_service.dto;

import com.project.registration_service.enumeration.Level;

public record UpdateRegDTO(
        String matricule,
        Level level
) {}
