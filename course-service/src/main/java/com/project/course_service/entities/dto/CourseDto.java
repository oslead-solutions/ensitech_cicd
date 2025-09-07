package com.project.course_service.entities.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CourseDto {
    private Long id;
    @NotBlank private String title;
    @NotNull
    private Integer coefficient;
    @NotNull private Integer hours;

    // --- Champs enrichis ---
    // Ces objets viendront de l'appel OpenFeign
    // Pour la réponse (GET) : objets enrichis

    private Teacher teacher;
    private Set<Student> students;

    // --- Champs pour la création/mise à jour ---
    // Le client enverra ces IDs pour faire les associations.
    // Pour la requête (POST/PUT) : on ne reçoit que les IDs
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long teacherId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Long> studentIds;
}
