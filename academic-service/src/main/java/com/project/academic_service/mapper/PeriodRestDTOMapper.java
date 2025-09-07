package com.project.academic_service.mapper;

import com.project.academic_service.domain.Period;
import com.project.academic_service.dto.PeriodRestDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PeriodRestDTOMapper implements Function<Period, PeriodRestDTO> {
    @Override
    public PeriodRestDTO apply(Period period) {
        return new PeriodRestDTO(
                period.getEntitled(),
                period.getTypePeriod()

        );
    }
}
