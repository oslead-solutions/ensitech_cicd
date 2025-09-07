package com.project.course_service.entities.dto;

import com.project.course_service.enumeration.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//"double" de DTO de user-service.

@Data
public class Student {
    private Long id; // L'identifiant unique de l'enseignant
    private String firstName;
    private  String lastName;
    @Email(message = "Le format de l'email est invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;
    private String matricule;
    /*private String address;
    private String telephone;
    private Date birthday;
    private Gender gender;*/
}
