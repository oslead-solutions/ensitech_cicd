package com.project.user_service.service.mapper;


import com.project.user_service.models.dto.TeacherDto;
import com.project.user_service.models.entities.Teacher;
import org.mapstruct.Mapper;


import java.util.List;

//Indique à MapStruct de générer une implémentation qui est un bean Spring (@Component), pour que nous puissions l'injecter
// dans nos services.
@Mapper(componentModel = "spring")
public interface TeacherMapper {
    /**
     * Convertit une entité Teacher en TeacherDto.
     * Les champs avec des noms identiques sont mappés automatiquement.
     * Le champ 'courses' est ignoré car il n'est pas dans le DTO.
     */
    TeacherDto toDto(Teacher teacher);

    /**
     * Convertit un TeacherDto en entité Teacher.
     * On ignore les champs 'id', 'createdAt' et 'courses' car ils ne doivent pas
     * provenir du client lors de la création/mise à jour.
     */

    Teacher toEntity(TeacherDto teacherDto);

    /**
     * MapStruct génère automatiquement la boucle pour convertir une liste d'entités.
     */
    List<TeacherDto> toDtoList(List<Teacher> teachers);
}
