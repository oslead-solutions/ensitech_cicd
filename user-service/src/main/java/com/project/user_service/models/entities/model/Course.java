package com.project.user_service.models.entities.model;

import com.project.user_service.models.entities.Teacher;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class Course {

    private Long id;
    private String intitule;
    private Integer coefficient;
    private Integer nombreHeures;
    private Teacher teacher;
}
