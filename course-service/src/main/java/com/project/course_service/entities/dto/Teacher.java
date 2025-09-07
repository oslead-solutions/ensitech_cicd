package com.project.course_service.entities.dto;

import com.project.course_service.enumeration.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

//"double" de DTO de user-service.

@Getter
@Setter
public class Teacher {
    private Long id;
    private String firstName;
    private  String lastName;
    @Email(message = "Le format de l'email est invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;
  /*  private String address;
    private String telephone;
    private Date birthday;
    private Gender gender;*/


}
