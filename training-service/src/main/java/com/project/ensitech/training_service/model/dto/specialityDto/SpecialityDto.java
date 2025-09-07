package com.project.ensitech.training_service.model.dto.specialityDto;

import com.project.ensitech.training_service.model.dto.courseDto.CourseDto;
import com.project.ensitech.training_service.model.enumeration.Cycle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialityDto {
    @NotNull(message = "L'identifant est obligatoire")
    private Long id;
    @NotBlank(message = "Le label  est obligatoire")
    private  String label;
    private String description;
    @NotNull(message = "Le cycle  est obligatoire")
    private Cycle cycle;
    // private Set<Long> courseIds;
    private Set<CourseDto> courses;
}
