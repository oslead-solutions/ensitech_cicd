package com.project.user_service.service.implementation;

import com.project.user_service.exception.EmailAlreadyExistsException;
import com.project.user_service.exception.ResourceNotFoundException;
import com.project.user_service.models.dto.TeacherDto;
import com.project.user_service.models.entities.Teacher;
import com.project.user_service.repository.PersonRepository;
import com.project.user_service.service.common.ITeacherService;
import com.project.user_service.service.mapper.TeacherMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@Service
public class TeacherServiceImpl implements ITeacherService {

    // Initialisation du logger pour cette classe en utilisant Log4j2
    private static final Logger log = LogManager.getLogger(TeacherServiceImpl.class);

    private final PersonRepository personRepository;
    private final TeacherMapper teacherMapper; //

    public TeacherServiceImpl(PersonRepository personRepository, TeacherMapper teacherMapper) {
        this.personRepository = personRepository;
        this.teacherMapper = teacherMapper;
    }

    /**
     * Crée un nouvel enseignant à partir des données d'un DTO.
     *
     * @param dto Les informations du nouvel enseignant.
     * @return L'entité Teacher qui a été sauvegardée.
     */
    @Override
    @Transactional // Les opérations d'écriture doivent être transactionnelles
    public TeacherDto createTeacher(TeacherDto dto) {
        log.info("Tentative de création d'un nouveau enseignant avec email: {}", dto.getEmail());
        // 1. Traduire le DTO en Entité
        Teacher teacher = teacherMapper.toEntity(dto);
        if (personRepository.findByEmail(teacher.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("L'email " + teacher.getEmail() + " est déjà utilisé par un autre utilisateur");
        }
        teacher.setCreatedAt(new Date());

        // 2. Sauvegarder l'entité
        Teacher savedTeacher = personRepository.save(teacher);
        log.info("Enseignant créer avec succès avec ID: {}", savedTeacher.getId());

        // 3. Traduire l'entité sauvegardée en DTO pour la réponse
        return teacherMapper.toDto(savedTeacher);
    }

    /**
     * Récupère la liste de tous les enseignants.
     *
     * @return une liste d'entités Teacher.
     */
    @Override
    @Transactional(readOnly = true) // Transaction en lecture seule, c'est optimisé !
    public Collection<TeacherDto> getAllTeachers() {
        log.info(" Recupération de tous les enseignants de la base de donnée.");
        List<Teacher> teachers = personRepository.findAllTeachers();

        log.info("Trouvé {} enseignants.", teachers.size());
        // Le mapping se fait ici, PENDANT que la transaction est ouverte.
        // C'est ce qui résout la LazyInitializationException !
        return teacherMapper.toDtoList(teachers);
    }

    /**
     * Récupère un enseignant par son identifiant.
     *
     * @param id L'identifiant de l'enseignant.
     * @return L'enseignant trouvé.
     * @throws RuntimeException si aucun enseignant n'est trouvé avec cet ID.
     */
    @Override
    @Transactional(readOnly = true)
    public TeacherDto getTeacherById(Long id) {
        log.info("Recupération de l'enseignant avec ID: {}", id);
        Teacher teacher = personRepository.findTeacherById(id)
                .orElseThrow(() -> {
            // On log l'échec avant de lancer l'exception
            log.warn("Enseignant non trouvé avec l'identifiant ID: {}", id);
            return new ResourceNotFoundException("Enseignant non trouvé avec l'identifiant : " + id);
        });
        return teacherMapper.toDto(teacher);
    }

    /**
     * Met à jour les informations d'un enseignant existant.
     *
     * @param id  L'identifiant de l'enseignant à mettre à jour.
     * @param dto Les nouvelles informations.
     * @return L'entité Teacher mise à jour.
     */
    @Override
    @Transactional
    public TeacherDto updateTeacher(Long id, TeacherDto dto) {
        log.info(" Tentative de mise à jour de l'enseignant avec ID: {}", id);
        Teacher existingTeacher = personRepository.findTeacherById(id)
                .orElseThrow(() ->  {
                    log.warn("Mise à jour échouée. Enseignant non trouvé avec ID: {}", id);
                    return new ResourceNotFoundException("Enseignant non trouvé avec l'identifiant : " + id);
                });

        // Mise à jour (un mapper plus avancé pourrait le faire aussi)
        existingTeacher.setFirstName(dto.getFirstName());
        existingTeacher.setLastName(dto.getLastName());
        existingTeacher.setEmail(dto.getEmail());
        existingTeacher.setAddress(dto.getAddress());
        existingTeacher.setTelephone(dto.getTelephone());
        existingTeacher.setBirthday(dto.getBirthday());
        existingTeacher.setGender(dto.getGender());

        Teacher updatedTeacher = personRepository.save(existingTeacher);
        log.info(" Mise à jour réussie de l'enseignant avec ID: {}", updatedTeacher.getId());
        return teacherMapper.toDto(updatedTeacher);
    }

    /**
     * Supprime un enseignant par son identifiant.
     *
     * @param id L'identifiant de l'enseignant à supprimer.
     */
    @Override
    @Transactional
    public void deleteTeacher(Long id) {
        // On essaie de trouver l'entité. Si elle n'existe pas,
        // orElseThrow lèvera directement l'exception.

        Teacher teacherToDelete = personRepository.findTeacherById(id)
                .orElseThrow(() -> {
                    log.warn("Suppression échouée. Enseignant non trouvé avec ID: {}", id);
                    return new ResourceNotFoundException("Impossible de supprimer. Enseignant non trouvé avec l'identifiant : " + id);
                });

        // Si on arrive ici, l'enseignant existe. On peut le supprimer.
        personRepository.delete(teacherToDelete); // Utiliser delete(entity) est souvent plus sûr.

        log.info("Suppression avec succès de l'enseignant avec ID: {}", id);
    }


}
