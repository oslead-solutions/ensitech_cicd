//Ce mapper est intelligent. Il sait que teacher et students sur le DTO ne viennent pas directement de l'entité.
package com.project.course_service.service.mappeur;
import com.project.course_service.entities.Course;
import com.project.course_service.entities.dto.CourseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    // On ignore les champs qui seront remplis manuellement par le service
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "students", ignore = true)
    CourseDto toDto(Course course);

    // On mappe les IDs du DTO vers l'entité
    @Mapping(target = "teacherId", source = "teacherId")
    @Mapping(target = "studentIds", source = "studentIds")
    Course toEntity(CourseDto dto);

    List<CourseDto> toDtoList(List<Course> courses);
}