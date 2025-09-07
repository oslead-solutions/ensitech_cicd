package com.project.academic_service.mapper;

import com.project.academic_service.domain.AcademicYear;
import com.project.academic_service.dto.AcademicDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Component
public class AcademicYearUpdateMapper implements BiFunction<AcademicDTO, AcademicYear, AcademicYear> {
    @Override
    public AcademicYear apply(AcademicDTO academicDTO, AcademicYear academicYear) {
        academicYear.setLabel(academicDTO.label());
        academicYear.setStartDate(academicDTO.startDate());
        academicYear.setEndDate(academicDTO.endDate());
        academicYear.setStatus(academicDTO.status());


        academicYear.setUpdatedAt(LocalDateTime.now());

        return academicYear;
    }
}
