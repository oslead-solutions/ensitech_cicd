package com.project.academic_service.dto;

import com.project.academic_service.enumeration.TypePeriod;

import java.time.LocalDate;

public record PeriodDTO(
        String entitled,
        TypePeriod typePeriod,
        LocalDate startedAt,
        LocalDate endedAt,
        Integer academicYearId
) {

}
