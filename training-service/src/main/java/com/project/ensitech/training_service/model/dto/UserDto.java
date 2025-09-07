package com.project.ensitech.training_service.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // Génère un constructeur sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les champs (utile pour les tests)
public class UserDto {
    private Long id; // L'identifiant unique de l'enseignant
    private String firstName;
    private  String lastName;
    private String email;
    private String telephone;
}
