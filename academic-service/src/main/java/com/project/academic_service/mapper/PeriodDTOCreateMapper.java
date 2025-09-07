package com.project.academic_service.mapper;

import com.project.academic_service.domain.Period;
import com.project.academic_service.dto.PeriodDTOCreate;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PeriodDTOCreateMapper implements Function <Period, PeriodDTOCreate> {
    @Override
    public PeriodDTOCreate apply(Period period) {
        return new PeriodDTOCreate(
                period.getEntitled(),
                period.getTypePeriod(),
                period.getStartedAt(),
                period.getEndedAt()
                );
    }
}
