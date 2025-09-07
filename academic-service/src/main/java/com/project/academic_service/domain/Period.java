package com.project.academic_service.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.academic_service.enumeration.TypePeriod;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Period {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "this field can't be blank")
    @Column(nullable = false)
    private String entitled;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_period", nullable = false)
    private TypePeriod typePeriod;


    @Column(name = "started_at", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startedAt;

    @Column(name = "ended_at", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endedAt;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime createdAt;

    @Column(name = "updated", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = true)
    @JsonBackReference
    private AcademicYear academicYear;

    @PrePersist
    protected void onCreate(){
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt=LocalDateTime.now();
    }


}
