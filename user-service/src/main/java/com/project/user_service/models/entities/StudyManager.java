package com.project.user_service.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true) // Important pour l'héritage !
@DiscriminatorValue("STUDYMANAGER") // Valeur pour le StudyManager
public class StudyManager extends Person{
    @Column(name = "password", nullable = false)
    private String password;
}
