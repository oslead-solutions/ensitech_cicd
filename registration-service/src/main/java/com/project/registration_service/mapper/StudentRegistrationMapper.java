package com.project.registration_service.mapper;

import com.project.registration_service.domain.Registration;
import com.project.registration_service.dto.*;
import com.project.registration_service.enumeration.Level;
import com.project.registration_service.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentRegistrationMapper {
    private StudentRegistrationMapper() {}


    public static RegistrationStudentDTO toDto(Registration entity) {
        if (entity == null) return null;
        return RegistrationStudentDTO.builder()
                .registrationNumber(String.valueOf(entity.getRegistrationNumber()))
                .level(entity.getLevel() == null ? null : String.valueOf(entity.getLevel()))
                .dateOfRegistration(entity.getDateOfRegistration().toLocalDate())
                .matricule(entity.getMatricule())
                .student(null) // on enrichira après via Feign
                .build();
    }
    public static RegistrationStudentDTO toDto(Registration entity, Student student) {
        if (entity == null) return null;

        return RegistrationStudentDTO.builder()
                .registrationNumber(entity.getRegistrationNumber() != null
                        ? String.valueOf(entity.getRegistrationNumber())
                        : null)
                .level(entity.getLevel() != null ? entity.getLevel().name() : null)
                .dateOfRegistration(entity.getDateOfRegistration() != null
                        ? entity.getDateOfRegistration().toLocalDate()
                        : null)
                .matricule(entity.getMatricule())
                .student(student) // <-- null si expandStudent=false
                .build();
    }
    public static Registration toEntity(CreateRegistrationDTO dto) {
        if(dto == null){
            return null;
        }
        return Registration.builder()
//                .registrationNumber(dto.registrationNumber())
                .matricule(dto.matricule())
                .specialityLabel(dto.specialityLabel())
                .level(dto.level())
                .academicYearLabel(dto.academicYearLabel())
                .build();
    }


    public static UpdateRegDTO toUpDateDto(Registration entity) {
        if (entity == null)
            return null;
        return new UpdateRegDTO(
                entity.getMatricule(),
                entity.getLevel()

        );
    }

    public static RegDTO toDtoR(Registration registration){

        return new RegDTO(
                registration.getRegistrationNumber(),
                registration.getLevel(),
                registration.getMatricule(),
                registration.getSpecialityLabel(),
                registration.getAcademicYearLabel(),
                registration.getStudent()
        );

    }

    public static Registration toEntityR(RegDTO regDTO){
        if(regDTO == null){
            throw new RuntimeException("Null Object Dto found");
        }

        Registration registration = new Registration();
//        registration.setRegistrationNumber(regDTO.registrationNumber());
        registration.setMatricule(regDTO.matricule());
        registration.setLevel(regDTO.level());
        registration.setSpecialityLabel(regDTO.specialityLabel());
        registration.setAcademicYearLabel(regDTO.academicYearLabel());
        registration.setStudent(regDTO.student());

        return registration;
    }

    public static List<RegDTO> toRegDTOList(List<Registration> registrations){
        if (registrations == null || registrations.isEmpty()) {
            return new ArrayList<>(); // Return an empty list for null or empty input
        }
        return registrations.stream()
                .map(StudentRegistrationMapper::toDtoR).
                collect(Collectors.toList());

    }

}
