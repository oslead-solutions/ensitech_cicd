package com.project.user_service.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.user_service.enumeration.Gender;
import com.project.user_service.models.entities.model.Course;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true) // Important pour l'héritage !
@DiscriminatorValue("TEACHER")
public class Teacher extends Person{
    @Temporal(TemporalType.TIMESTAMP) // Ici on veut la date et l'heure de création.
    @Column(name="created_at")//correspondance avec la bd
    private Date createdAt;


    // LE CONSTRUCTEUR EXACT QUE HIBERNATE RECHERCHE
    public Teacher(Long id, String firstName, String lastName, String email, String address, String telephone, Date birthday, Gender gender, Date createdAt) {
        super(id, firstName, lastName, email, address, telephone, birthday, gender);
        this.createdAt = createdAt;
    }

}
