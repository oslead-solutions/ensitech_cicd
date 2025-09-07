package com.project.user_service.service.common;

import com.project.user_service.models.dto.DirectorDto;

import java.util.List;

public interface IDirectorService {
    DirectorDto createDirector(DirectorDto dto);
    List<DirectorDto> getAllDirectors();
    DirectorDto getDirectorById(Long id);
    DirectorDto updateDirector(Long id, DirectorDto dto);
    void deleteDirector(Long id);
}
