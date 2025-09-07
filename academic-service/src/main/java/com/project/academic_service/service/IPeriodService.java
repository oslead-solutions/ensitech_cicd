package com.project.academic_service.service;

import com.project.academic_service.domain.Period;
import com.project.academic_service.dto.PeriodDTO;
import com.project.academic_service.dto.PeriodRestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPeriodService {
    PeriodDTO get(int id);
    List<PeriodDTO> getAll();
    List<PeriodRestDTO> getAllPeriod();
    Period create(PeriodDTO periodDTO);
    PeriodDTO update(int id, PeriodDTO periodDTO);
    void delete(int id);
}
