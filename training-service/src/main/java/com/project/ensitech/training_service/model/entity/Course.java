package com.project.ensitech.training_service.model.entity;

import com.project.ensitech.training_service.model.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "courses",indexes = @Index(columnList = "teacherId"))
@NoArgsConstructor
@AllArgsConstructor
// @Table(indexes = @Index(columnList = "teacherId"))
// Builder
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private int coefficient;
    private int hours;

    @ManyToMany(mappedBy = "courses")
    private Set<Speciality> specialities = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Evaluation> evaluations = new HashSet<>();

    private Long teacherId;
    @Transient
    private UserDto teacher;

   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return id != null && id.equals(course.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }*/
}