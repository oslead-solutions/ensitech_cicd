package com.project.user_service.service.common;

import com.project.user_service.models.dto.StudyManagerDto;

import java.util.List;

public interface IStudyManagerService {
    StudyManagerDto createStudyManager(StudyManagerDto dto);
    List<StudyManagerDto> getAllStudyManagers();
    StudyManagerDto getStudyManagerById(Long id);
    StudyManagerDto updateStudyManager(Long id, StudyManagerDto dto);
    void deleteStudyManager(Long id);
}
