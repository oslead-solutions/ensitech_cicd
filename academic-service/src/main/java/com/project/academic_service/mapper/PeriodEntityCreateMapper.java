package com.project.academic_service.mapper;

import com.project.academic_service.domain.Period;
import com.project.academic_service.dto.PeriodDTOCreate;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PeriodEntityCreateMapper implements Function<PeriodDTOCreate, Period> {

    @Override
    public Period apply(PeriodDTOCreate periodDTOCreate) {
        return Period.builder()
                .startedAt(periodDTOCreate.startedAt())
                .endedAt(periodDTOCreate.endedAt())
                .typePeriod(periodDTOCreate.typePeriod())
                .entitled(periodDTOCreate.entitled())
                .build();
    }
}
