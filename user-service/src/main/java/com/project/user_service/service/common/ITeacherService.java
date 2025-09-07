package com.project.user_service.service.common;

import com.project.user_service.models.dto.TeacherDto;

import java.util.Collection;


public interface ITeacherService {
    /**
     * Crée un nouvel enseignant à partir des données d'un DTO.
     * @param dto Les informations du nouvel enseignant.
     * @return L'entité Teacher qui a été sauvegardée.
     */
    TeacherDto createTeacher(TeacherDto dto);
    /**
     * Récupère la liste de tous les enseignants.
     * @return une liste d'entités Teacher.
     */
    Collection<TeacherDto> getAllTeachers();

    /**
     * Récupère un enseignant par son identifiant.
     * @param id L'identifiant de l'enseignant.
     * @return L'enseignant trouvé.
     * @throws RuntimeException si aucun enseignant n'est trouvé avec cet ID.
     */
    TeacherDto getTeacherById(Long id);

    /**
     * Met à jour les informations d'un enseignant existant.
     * @param id L'identifiant de l'enseignant à mettre à jour.
     * @param dto Les nouvelles informations.
     * @return L'entité Teacher mise à jour.
     */
    TeacherDto updateTeacher(Long id, TeacherDto dto);

    /**
     * Supprime un enseignant par son identifiant.
     * @param id L'identifiant de l'enseignant à supprimer.
     */
    void deleteTeacher(Long id);
}
