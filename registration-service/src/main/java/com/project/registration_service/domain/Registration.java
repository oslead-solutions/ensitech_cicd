package com.project.registration_service.domain;

import com.project.registration_service.enumeration.Level;
import com.project.registration_service.model.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter @Getter @ToString
@NoArgsConstructor @AllArgsConstructor @Builder
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long registrationNumber;

    @Enumerated(EnumType.STRING)
    private Level level;

    private LocalDateTime dateOfRegistration;

    private String matricule;

    private String specialityLabel;

    @Pattern(regexp = "^2[0-9]{3}-2[0-9]{3}$", message = "Format du label de l'année academique invalide")
    private String academicYearLabel;

    @Transient
    private Student student;

    @PrePersist
    void OnCreate(){
        this.dateOfRegistration = LocalDateTime.now();
    }

}
