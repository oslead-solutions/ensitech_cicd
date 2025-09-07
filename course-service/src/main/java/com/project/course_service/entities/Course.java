package com.project.course_service.entities;

import com.project.course_service.entities.dto.Student;
import com.project.course_service.entities.dto.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

// Modèle de base de données
@Data
@Entity
@Table(name = "course")// Nom de table explicite
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"students", "teachers"})// Exclure pour éviter les boucles
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer coefficient;

    @Column(nullable = false)
    private Integer hours;

    // --- RELATION One-to-Many (Teacher -> Course) ---
    // On ne stocke que l'ID de l'enseignant.
    private Long teacherId; //clé étrangère vu que l'entité n'est pas persistent

    // --- RELATION Many-to-Many (Student <-> Course) ---
    // La table de jointure sera dans cette base de données.
    // On ne stocke qu'une collection d'IDs d'étudiants.
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "course_students", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "student_id")
    private Set<Long> studentIds = new HashSet<>();

    @Transient//cet attribut existe dans la classe(dto) mais pas dans la bd donc n'est pas persistent
    private Teacher teacher;


    @Transient //non persistent
    private Set<Student> students = new HashSet<>();


}