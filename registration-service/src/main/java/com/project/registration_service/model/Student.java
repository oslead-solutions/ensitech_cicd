package com.project.registration_service.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

// representation miroir de Student.
// ceci est un dto aussi
@Getter @Setter @Builder
public class Student {
    private Long id; // Utiliser Long pour les IDs est une bonne pratique, pour éviter les dépassements.
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String telephone;
    private String matricule;
    private Date birthday;
    private String gender;


}
