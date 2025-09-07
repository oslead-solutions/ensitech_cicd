package com.project.academic_service.mapper;

import com.project.academic_service.domain.Period;
import com.project.academic_service.dto.PeriodDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PeriodEntityMapper implements Function<PeriodDTO, Period> {

    @Override
    public Period apply(PeriodDTO periodDTO) {
        Period period = new Period();
        period.setEntitled(periodDTO.entitled());
        period.setTypePeriod(periodDTO.typePeriod());
        period.setStartedAt(periodDTO.startedAt());
        period.setEndedAt(periodDTO.endedAt());
        return period;
    }
}
