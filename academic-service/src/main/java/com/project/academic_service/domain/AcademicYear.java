package com.project.academic_service.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.academic_service.enumeration.AcademicYearStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(name = "academic_year")
public class AcademicYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Pattern(regexp = "^2[0-9]{3}-2[0-9]{3}$", message = "Format du label de l'année academique invalide")
    private String label;

    @Column(name = "start_date", nullable = false)
    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDate startDate;

    @Column(name="ended_date", nullable = false)
    @JsonFormat(pattern = "MM-dd-yyyy")

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcademicYearStatus status;

    @Column(name="created_at")
    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "academicYear", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Period> periods = new ArrayList<>();

    @PrePersist
    protected void onCreate(){
        this.createdAt= LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt=LocalDateTime.now();
    }

    public void addPeriod(Period period){
        periods.add(period);
        period.setAcademicYear(this);
    }

    public void removePeriod(Period period){
        periods.remove(period);
        period.setAcademicYear(null);
    }

}
