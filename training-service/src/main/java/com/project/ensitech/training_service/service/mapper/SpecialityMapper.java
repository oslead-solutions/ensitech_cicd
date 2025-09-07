package com.project.ensitech.training_service.service.mapper;

import com.project.ensitech.training_service.model.dto.courseDto.CourseDto;
import com.project.ensitech.training_service.model.dto.specialityDto.SpecialityDto;
import com.project.ensitech.training_service.model.dto.specialityDto.CreateSpecialityDto;
import com.project.ensitech.training_service.model.entity.Course;
import com.project.ensitech.training_service.model.entity.Speciality;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpecialityMapper {
    Speciality toEntity(SpecialityDto dto);
    Speciality toEntity(CreateSpecialityDto dto);

    SpecialityDto toDto(Speciality entity);

    CourseDto toDto(Course course);
}

