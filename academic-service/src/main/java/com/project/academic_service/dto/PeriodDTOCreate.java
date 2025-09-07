package com.project.academic_service.dto;

import com.project.academic_service.enumeration.TypePeriod;

import java.time.LocalDate;

public record PeriodDTOCreate(
        String entitled,
        TypePeriod typePeriod,
        LocalDate startedAt,
        LocalDate endedAt
) {
}
