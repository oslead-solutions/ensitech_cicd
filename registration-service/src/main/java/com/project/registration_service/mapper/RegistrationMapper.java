package com.project.registration_service.mapper;

import com.project.registration_service.domain.Registration;
import com.project.registration_service.dto.RegistrationDTO;
import com.project.registration_service.dto.RegistrationStudentDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RegistrationMapper {
    public Registration toEntity(RegistrationDTO registrationDTO){
        if(registrationDTO == null)
            return null;
        Registration registration = new Registration();
            registration.setRegistrationNumber(registrationDTO.registrationNumber());
            registration.setLevel(registrationDTO.level());

        return registration;
    }

    public RegistrationDTO toDto(Registration registration){
        if (registration == null)
            return null;
        return new RegistrationDTO(
          registration.getRegistrationNumber(),
          registration.getLevel()
        );
    }

//    public RegistrationStudentDTO transformToDto(Registration registration){
//        if (registration == null)
//            return null;
//        return new RegistrationStudentDTO(
//                registration.getRegistrationNumber(),
//                registration.getLevel(),
//                registration.getDateOfRegistration(),
//
//        );
//    }

    // Without stream
    /*
    public List<RegistrationDTO> toDtoList(List<Registration> registrations){
        List<RegistrationDTO> registrationDTOList = new ArrayList<>();
        registrations.forEach( registration -> {
            RegistrationDTO registrationDTO= new RegistrationDTO(registration.getRegistrationNumber(), registration.getLevel());
            registrationDTOList.add(registrationDTO);
        });

        return registrationDTOList;

    }*/

    // With stream
    public List<RegistrationDTO> toDtoList(List<Registration> registrations){
        if (registrations == null || registrations.isEmpty()) {
            return new ArrayList<>(); // Return an empty list for null or empty input
        }
        return registrations.stream()
                .map(this::toDto).
                collect(Collectors.toList()); // Reuses your existing toDto method

    }


}
