package com.project.ensitech.training_service.service.common;

import com.project.ensitech.training_service.model.dto.specialityDto.SpecialityDto;
import com.project.ensitech.training_service.model.dto.specialityDto.CreateSpecialityDto;

import java.util.List;

public interface ISpecialityService {
    SpecialityDto createSpeciality(CreateSpecialityDto dto);
    SpecialityDto getSpeciality(Long id);
    SpecialityDto updateSpeciality(SpecialityDto dto);
    void deleteSpeciality(Long id);
    List<SpecialityDto> searchByLabel(String label);
    List<SpecialityDto> getAllSpecialities();
    SpecialityDto getSpecialityByLabel(String label);
}
