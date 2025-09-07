package com.project.registration_service.service;

import com.project.registration_service.dto.*;

import java.util.List;

public interface RegistrationService {
    RegistrationDTO getRegistration(Long id);
    List<RegistrationDTO> getAllRegistrations();
    RegistrationDTO createRegistration(RegistrationDTO registrationDTO);
    RegistrationDTO updateRegistration(Long id, RegistrationDTO registrationDTO);

    RegDTO updateRegs(Long id, UpdateRegDTO updateRegDTO);

    void deleteRegistration(Long id);
    RegDTO getById(Long id);
    RegDTO getRegsByMatriculeAndAYLabel(String matricule, String label);
    RegDTO processRegistration(CreateRegistrationDTO createRegistrationDTO);
    List<RegDTO> getRegistrationListByMatricule(String matricule);
    List<RegDTO> getAllRegs();
    List<RegDTO> getRegistrationsByLabel(String label);

}
