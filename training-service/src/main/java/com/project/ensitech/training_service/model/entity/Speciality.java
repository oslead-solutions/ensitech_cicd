package com.project.ensitech.training_service.model.entity;

import com.project.ensitech.training_service.model.enumeration.Cycle;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "specialties")
@NoArgsConstructor
@AllArgsConstructor
public class Speciality {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String label;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Cycle cycle;

    @ManyToMany
    @JoinTable(name = "speciality_courses",
            joinColumns = @JoinColumn(name="speciality_id"),
            inverseJoinColumns = @JoinColumn(name="course_id")
    )
    private Set<Course> courses = new HashSet<>();
    //private Set<Long> courses = new HashSet<>();

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Speciality)) return false;
        Speciality that = (Speciality) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }*/
}
