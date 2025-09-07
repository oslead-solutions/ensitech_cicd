package com.project.registration_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.registration_service.enumeration.AcademicYearStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter @Builder
public class AcademicYear {
    private String label;
    private LocalDate startDate;
    private LocalDate endDate;
    private AcademicYearStatus status;
    private List<Period> Periods;

}
