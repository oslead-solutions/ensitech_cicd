package com.project.academic_service.mapper;

import com.project.academic_service.domain.AcademicYear;
import com.project.academic_service.dto.AcademicYearRestDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
@AllArgsConstructor
public class AcademicYearRestMapper implements Function<AcademicYear, AcademicYearRestDTO> {

    private PeriodDTOMapper periodDTOMapper;
    private  PeriodDTOCreateMapper periodDTOCreateMapper;
    @Override
    public AcademicYearRestDTO apply(AcademicYear academicYear) {

        if(academicYear.getPeriods() == null){
            throw new NullPointerException("Periods can't be Null");
        }
        return  new AcademicYearRestDTO(
                academicYear.getLabel(),
                academicYear.getStartDate(),
                academicYear.getEndDate(),
                academicYear.getStatus(),
                academicYear.getPeriods()
                        .stream()
                        .map(periodDTOCreateMapper)
                        .toList()

        );
    }
}
