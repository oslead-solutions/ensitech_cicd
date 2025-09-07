package com.project.ensitech.training_service.service.mapper;

import com.project.ensitech.training_service.model.dto.courseDto.CourseDto;
import com.project.ensitech.training_service.model.dto.courseDto.CreateCourseDto;
import com.project.ensitech.training_service.model.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toEntity(CourseDto dto);
    Course toEntity(CreateCourseDto dto);
    CourseDto toDto(Course entity);
}

