package com.project.academic_service.mapper;

import com.project.academic_service.domain.AcademicYear;
import com.project.academic_service.dto.AcademicDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Function;

@Component
public class AcademicEntityMapper implements Function<AcademicDTO, AcademicYear> {

//    private final PeriodEntityMapper periodEntityMapper;
    private final PeriodEntityCreateMapper periodEntityCreateMapper;

    public AcademicEntityMapper(PeriodEntityCreateMapper periodEntityCreateMapper) {
        this.periodEntityCreateMapper = periodEntityCreateMapper;
    }

    @Override
    public AcademicYear apply(AcademicDTO academicDTO) {
        AcademicYear academicYear = new AcademicYear();
        academicYear.setLabel(academicDTO.label());
        academicYear.setStartDate(academicDTO.startDate());
        academicYear.setEndDate(academicDTO.endDate());
        academicYear.setStatus(academicDTO.status());

        academicYear.setCreatedAt(LocalDateTime.now());
        academicYear.setUpdatedAt(LocalDateTime.now());

        if (academicDTO.periods() != null)
            academicDTO.periods().forEach(periodCreateDTO -> academicYear.addPeriod(this.periodEntityCreateMapper.apply(periodCreateDTO)));

        return academicYear;
    }
}
