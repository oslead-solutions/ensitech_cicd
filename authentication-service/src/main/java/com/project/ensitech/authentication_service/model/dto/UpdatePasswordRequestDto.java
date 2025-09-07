package com.project.ensitech.authentication_service.model.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordRequestDto {

    @NotBlank(message = "L'email  est obligatoire")
    @Email(message = "Email doit être valide")
    private String email;

    @NotBlank(message = "Ancien mot de passe requis")
    @Size(min = 6, message = "Le mot de passe doit avoir au moins  6 characters")
    private String oldPassword;

    @NotBlank(message = "Nouveau mot de passe requis")
    @Size(min = 6, message = "Le mot de passe doit avoir au moins  6 characters")
    private String newPassword;
}
