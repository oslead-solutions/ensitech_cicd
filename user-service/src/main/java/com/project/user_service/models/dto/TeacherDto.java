package com.project.user_service.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.user_service.enumeration.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data  // Génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // Génère un constructeur sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les champs (utile pour les tests)
public class TeacherDto {
    private Long id; // L'identifiant unique de l'enseignant

    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "Le nom de famille est obligatoire")
    private String lastName;

    @Email(message = "Le format de l'email est invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    private String address;
    private String telephone;

    @Past(message = "La date de naissance doit être dans le passé")
    private Date birthday;

    @NotNull(message = "Le genre est obligatoire")
    private Gender gender;

    private Date createdAt;
}
