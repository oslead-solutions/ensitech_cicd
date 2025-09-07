package com.project.academic_service.dto;

import com.project.academic_service.domain.Period;
import com.project.academic_service.enumeration.AcademicYearStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record AcademicDTO(
        Integer id,
         String label,
         LocalDate startDate,
         LocalDate endDate,
         AcademicYearStatus status,
         List<PeriodDTOCreate> periods
         ) {}
