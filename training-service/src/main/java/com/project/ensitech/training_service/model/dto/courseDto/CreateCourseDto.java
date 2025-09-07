package com.project.ensitech.training_service.model.dto.courseDto;

import com.project.ensitech.training_service.model.dto.UserDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseDto {
    @NotBlank(message = "Le titre  est obligatoire")
    private String title;
    @NotNull(message = "Le coefficient  est obligatoire")
    private int coefficient;
    @NotNull(message = "Le nombre d'heure est obligatoire")
    private int hours;
    private Long teacherId;
    private UserDto teacher;

}
