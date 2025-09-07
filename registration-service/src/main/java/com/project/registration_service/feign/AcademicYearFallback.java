package com.project.registration_service.feign;

import com.project.registration_service.enumeration.AcademicYearStatus;
import com.project.registration_service.model.AcademicYear;
import com.project.registration_service.model.Period;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AcademicYearFallback implements AcademicRestClient{
    @Override
    public AcademicYear getAcademicYearByLabel(String label) {
        return AcademicYear.builder()
                .label("2025-2026")
                .startDate(LocalDate.from(LocalDateTime.of(2025,8,14,12,35)))
                .endDate(LocalDate.from(LocalDateTime.of(2026,12,4,12,30)))
                .status(AcademicYearStatus.EN_PREPARATION)
                .build();
    }

    @Override
    public List<Period> getAllPeriods(){
        return null;
    }


}
