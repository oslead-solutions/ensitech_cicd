package com.project.academic_service.mapper;

import com.project.academic_service.domain.Period;
import com.project.academic_service.dto.PeriodDTO;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component
public class PeriodUpdateMapper implements BiFunction<PeriodDTO, Period, Period> {

    @Override
    public Period apply(PeriodDTO periodDTO, Period period) {
        period.setEntitled(periodDTO.entitled());
        period.setTypePeriod(periodDTO.typePeriod());
        period.setStartedAt(periodDTO.startedAt());
        period.setEndedAt(periodDTO.endedAt());
//        period.getAcademicYear().setId(periodDTO.academicYearId());
        return period;
    }
}
