package com.project.academic_service.dto;

import com.project.academic_service.enumeration.AcademicYearStatus;

import java.time.LocalDate;
import java.util.List;

public record AcademicYearRestDTO(
        String label,
        LocalDate startDate,
        LocalDate endDate,
        AcademicYearStatus status,
        List<PeriodDTOCreate> periods
) {
}
