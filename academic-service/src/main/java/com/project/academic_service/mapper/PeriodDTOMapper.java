package com.project.academic_service.mapper;

import com.project.academic_service.domain.Period;
import com.project.academic_service.dto.PeriodDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Component
public class PeriodDTOMapper implements Function<Period, PeriodDTO> {
    @Override
    public PeriodDTO apply(Period period) {
        return new PeriodDTO(
                period.getEntitled(),
                period.getTypePeriod(),
                period.getStartedAt(),
                period.getEndedAt(),
                period.getAcademicYear().getId()
        );
    }
}
