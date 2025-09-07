package com.project.user_service.service.common;

import com.project.user_service.models.dto.AdministratorDto;
import com.project.user_service.models.dto.AdministratorDto;
import com.project.user_service.models.entities.Administrator;

import java.util.List;

public interface IAdministratorService {
    AdministratorDto createAdministrator(AdministratorDto dto);
    List<AdministratorDto> getAllAdministrators();
    AdministratorDto getAdministratorById(Long id);
    AdministratorDto updateAdministrator(Long id, AdministratorDto dto);
    void deleteAdministrator(Long id);
}
