package com.project.course_service.service.implementation;

import com.project.course_service.entities.Course;
import com.project.course_service.entities.dto.CourseDto;
import com.project.course_service.entities.dto.Teacher;
import com.project.course_service.exception.ResourceAlreadyExistsException;
import com.project.course_service.exception.ResourceNotFoundException;
import com.project.course_service.feign.UserRestClient;
import com.project.course_service.repositories.CourseRepository;
import com.project.course_service.service.common.ICourseService;
import com.project.course_service.service.mappeur.CourseMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CourseServiceImpl implements ICourseService {
    private final CourseRepository courseRepository;
    private final UserRestClient userRestClient;
    private final CourseMapper courseMapper;
    // --- Méthodes CRUD de base ---

    @Override
    @Transactional
    public CourseDto createCourse(CourseDto dto) {
        log.info("Création d'un cours avec le titre: {}", dto.getTitle());
        Course course = courseMapper.toEntity(dto);

        // --- CORRECTION DE LA LOGIQUE DE VÉRIFICATION ---
        // On vérifie si un cours avec ce titre existe déjà AVANT de faire quoi que ce soit d'autre.
        if (courseRepository.existsByTitle(dto.getTitle())) {
            log.warn("Tentative de création d'un cours avec un titre existant : {}", dto.getTitle());
            throw new ResourceAlreadyExistsException("Un cours avec le titre '" + dto.getTitle() + "' existe déjà.");
        }

        // Si un teacherId est fourni à la création, on le valide et on l'assigne.
        if(dto.getTeacherId() != null) {
            validateTeacherExists(dto.getTeacherId());
            course.setTeacherId(dto.getTeacherId());
        }

        Course savedCourse = courseRepository.save(course);
        log.info("Cours créé avec succès. ID: {}", savedCourse.getId());
        // Pas besoin d'enrichir à la création, on retourne les IDs.
        return courseMapper.toDto(savedCourse);
    }

    @Override
    @Transactional
    public CourseDto updateCourse(Long courseId, CourseDto dto) {
        log.info("Mise à jour du cours ID: {}", courseId);
        Course existingCourse = findCourseById(courseId);

        existingCourse.setTitle(dto.getTitle());
        existingCourse.setCoefficient(dto.getCoefficient());
        existingCourse.setHours(dto.getHours());

        Course updatedCourse = courseRepository.save(existingCourse);
        return courseMapper.toDto(updatedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDto getCourseById(Long id) {
        log.info("Récupération du cours ID: {}", id);
        Course course = findCourseById(id);
        return enrichDto(courseMapper.toDto(course), course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getAllCourses() {
        log.info("Récupération de tous les cours.");
        List<Course> courses = courseRepository.findAll();
        // On enrichit chaque cours de la liste
        return courses.stream()
                .map(course -> {
                    CourseDto dto = courseMapper.toDto(course); // <-- Si courseMapper est null, ça plante ici.
                    return enrichDto(dto, course); // enrichDto utilise userRestClient.
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cours non trouvé avec l'ID : " + id);
        }
        courseRepository.deleteById(id);
        log.info("Cours ID: {} supprimé avec succès.", id);
    }

    // --- Méthodes d'association ---


    /**
     * Assigne un enseignant à un cours existant.
     * Cette méthode suit les bonnes pratiques de gestion d'erreurs et de transactions.
     * @param courseId L'ID du cours à modifier.
     * @param teacherId L'ID de l'enseignant à assigner.
     * @return Le DTO du cours mis à jour et enrichi avec les informations de l'enseignant.
     */
    @Override
    @Transactional // L'opération modifie la base de données, elle doit être transactionnelle.
    public CourseDto assignTeacherToCourse(Long courseId, Long teacherId) {
        log.info("Début - Association de l'enseignant ID {} au cours ID {}", teacherId, courseId);

        // --- Étape 1 : Valider les entrées (partie de l'Arrange/Act) ---

        // On récupère le cours depuis la base. Si non trouvé, on lance une exception métier claire.
        Course courseFromDb = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.warn("Échec : Cours non trouvé avec l'ID {}", courseId);
                    return new ResourceNotFoundException("Cours non trouvé avec l'ID : " + courseId);
                });

        // On valide que l'enseignant existe en appelant l'autre microservice.
        // C'est aussi une bonne pratique de créer une méthode privée pour cette logique.
        validateTeacherExists(teacherId);

        // --- Étape 2 : Appliquer la logique métier (partie de l'Act) ---

        // On met à jour l'ID de l'enseignant sur l'entité.
        courseFromDb.setTeacherId(teacherId);

        // On sauvegarde l'entité modifiée.
        Course savedCourse = courseRepository.save(courseFromDb);
        log.info("Association en base de données réussie pour le cours ID {}", courseId);

        // --- Étape 3 : Préparer la réponse (partie de l'Assert/Retour) ---

        // On convertit l'entité sauvegardée en DTO.
        CourseDto courseDto = courseMapper.toDto(savedCourse);

        // On enrichit le DTO avec l'objet Teacher complet (récupéré une deuxième fois, ou on pourrait optimiser).
        // C'est cette étape qui remplit le champ 'teacher' dans le DTO.
        enrichDtoWithTeacher(courseDto, savedCourse.getTeacherId());

        return courseDto;
    }

    // --- Méthodes privées pour la clarté ---

    private void validateTeacherExists(Long teacherId) {
        try {
            log.debug("Validation de l'enseignant ID {} via Feign.", teacherId);
            userRestClient.getTeacherById(teacherId);
        } catch (FeignException.NotFound e) {
            log.warn("Validation échouée : Enseignant non trouvé avec ID {}", teacherId);
            throw new ResourceNotFoundException("Validation échouée : Enseignant non trouvé avec l'ID : " + teacherId);
        }
    }


    @Override
    @Transactional
    public CourseDto enrollStudentInCourse(Long courseId, Long studentId) {
        log.info("Inscription de l'étudiant ID {} au cours ID {}", studentId, courseId);
        // 1. Valider que l'étudiant existe en appelant user-service.
        //    Si l'étudiant n'existe pas, le client Feign lèvera une exception
        //    et la transaction sera annulée (rollback).
        validateStudentExists(studentId); // Valide que l'étudiant existe

        // 2. Récupérer l'entité Course depuis la base de données locale.
        Course course = findCourseById(courseId);

        // 3. Ajouter l'ID de l'étudiant à la collection.
        //    L'utilisation d'un Set gère automatiquement les doublons (un étudiant ne peut pas être inscrit deux fois).
        boolean added = course.getStudentIds().add(studentId);
        if (!added) {
            log.warn("L'étudiant ID {} est déjà inscrit au cours ID {}", studentId, courseId);
            // On ne lève pas d'erreur, l'état final est correct.
        }

        // 4. Sauvegarder l'entité Course mise à jour.
        Course savedCourse = courseRepository.save(course);
        log.info("Étudiant ID {} inscrit avec succès au cours ID {}", studentId, courseId);

        // 5. Enrichir le DTO de retour avec les détails complets et le renvoyer.
        return enrichDto(courseMapper.toDto(savedCourse), savedCourse);
    }

    @Override
    @Transactional
    public CourseDto removeStudentFromCourse(Long courseId, Long studentId) {
        log.info("Désinscription de l'étudiant ID {} du cours ID {}", studentId, courseId);
        // 1. Récupérer l'entité Course.
        Course course = findCourseById(courseId);
        // 2. Retirer l'ID de l'étudiant de la collection.
        boolean removed = course.getStudentIds().remove(studentId);
        if (!removed) {
            log.warn("L'étudiant ID {} n'était pas inscrit au cours ID {}", studentId, courseId);
            // Pas besoin de lever une erreur, l'état final est correct.
        }
        // 3. Sauvegarder les changements.
        Course savedCourse = courseRepository.save(course);
        log.info("Étudiant ID {} désinscrit avec succès du cours ID {}", studentId, courseId);

        // 4. Enrichir et retourner le DTO mis à jour.
        return enrichDto(courseMapper.toDto(savedCourse), savedCourse);
    }

    // --- Méthodes utilitaires privées ---

    private Course findCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours non trouvé avec l'ID : " + courseId));
    }
    private void enrichDtoWithTeacher(CourseDto dto, Long teacherId) {
        if (teacherId != null) {
            try {
                log.debug("Enrichissement du DTO avec les détails de l'enseignant ID {}", teacherId);
                // On appelle Feign pour récupérer l'objet Teacher complet
                Teacher teacher = userRestClient.getTeacherById(teacherId);
                // On le place dans le DTO pour la réponse finale
                dto.setTeacher(teacher);
            } catch (Exception e) {
                log.error("Impossible de récupérer les détails de l'enseignant ID {} pour l'enrichissement.", teacherId, e);
                // On ne bloque pas la réponse, mais on logue l'erreur.
            }
        }
    }

    private void validateStudentExists(Long studentId) {
        try {
            // L'appel à Feign sert de validation. S'il échoue, une exception est levée.
            userRestClient.getStudentById(studentId);
        } catch (Exception e) {
            // On "traduit" l'exception technique de Feign en une exception métier claire.
            throw new ResourceNotFoundException("Validation échouée : Étudiant non trouvé avec l'ID : " + studentId);
        }
    }
    private CourseDto enrichDto(CourseDto dto, Course course) {
        // Enrichir avec le teacher via Feign
        if (course.getTeacherId() != null) {
            try {
                dto.setTeacher(userRestClient.getTeacherById(course.getTeacherId()));
            } catch (Exception e) { log.warn("Impossible de charger l'enseignant ID {}", course.getTeacherId()); }
        }

        // Enrichir avec les students via Feign (un seul appel réseau)
        if (course.getStudentIds() != null && !course.getStudentIds().isEmpty()) {
            try {
                dto.setStudents(new HashSet<>(userRestClient.getStudentsByIds(List.copyOf(course.getStudentIds()))));
            } catch (Exception e) { log.warn("Impossible de charger les étudiants pour le cours ID {}", course.getId()); }
        }
        return dto;
    }
}
