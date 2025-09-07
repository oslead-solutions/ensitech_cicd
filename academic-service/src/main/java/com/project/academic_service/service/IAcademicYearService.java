package com.project.academic_service.service;

import com.project.academic_service.domain.AcademicYear;
import com.project.academic_service.dto.AcademicDTO;
import com.project.academic_service.dto.AcademicYearRestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAcademicYearService {
    AcademicDTO get(int id);
    List<AcademicDTO> getYears();
    AcademicYear addAcademicYear(AcademicDTO academicDTO);
    AcademicYear create(AcademicYear academicYear);
    AcademicYear update(int id, AcademicYear academicYear);
    AcademicDTO updateAcademicYear(int id, AcademicDTO academicDTO);
    void delete(int id);
    AcademicYearRestDTO getAcademicYearByLabel(String label);
}
