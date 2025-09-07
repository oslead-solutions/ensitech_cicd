package com.project.user_service.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.user_service.enumeration.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class DirectorDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    // ... autres champs comme StudentDto ...
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Le mot de passe est en écriture seule
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;
}
