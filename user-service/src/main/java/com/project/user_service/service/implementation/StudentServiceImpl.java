package com.project.user_service.service.implementation;

import com.project.user_service.enumeration.Gender;
import com.project.user_service.exception.EmailAlreadyExistsException;
import com.project.user_service.exception.ResourceNotFoundException;
import com.project.user_service.models.dto.StudentDto;
import com.project.user_service.models.entities.Student;

import com.project.user_service.repository.PersonRepository;
import com.project.user_service.service.common.IStudentService;
import com.project.user_service.service.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // Injection de dépendances via le constructeur, gérée par Lombok. C'est moderne et propre.

public class StudentServiceImpl implements IStudentService {
    // Initialisation du logger pour cette classe en utilisant Log4j2
    final Logger log = LogManager.getLogger(StudentServiceImpl.class);

    private final PersonRepository personRepository;
    private final StudentMapper studentMapper;

    @Transactional // Les opérations d'écriture doivent être transactionnelles
    @Override
    public StudentDto createStudent(StudentDto studentDto) {

        log.info("Tentative de création d'un nouvel étudiant avec email: {}", studentDto.getEmail());

        Student student =studentMapper.toEntity(studentDto );
        if (personRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("L'email " + student.getEmail() + " est déjà utilisé par un autre utilisateur");
        }

        // --- LOGIQUE MÉTIER : Génération du matricule ---
        // Exemple : ENS- suivi de 8 caractères aléatoires en majuscules.
        String matricule = "ENS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        student.setMatricule(matricule);
        log.debug("Matricule généré pour l'étudiant {}: {}", studentDto.getEmail(), matricule);


        Student savedStudent = personRepository.save(student);
        log.info("Étudiant créé avec succès. ID: {}, Matricule: {}", savedStudent.getId(), savedStudent.getMatricule());

        return studentMapper.toDto(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getAllStudents() {
        log.info("Récupération de tous les étudiants.");
        List<Student> students = personRepository.findAllStudents();
        log.info("{} étudiants trouvés.", students.size());
        return studentMapper.toDtoList(students);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto getStudentById(Long id) {
        log.info("Recherche de l'étudiant avec ID: {}", id);
        return personRepository.findStudentById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Étudiant non trouvé avec ID: {}", id);
                    return new ResourceNotFoundException("Étudiant non trouvé avec l'identifiant : " + id);
                });
    }

    @Override
    @Transactional
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        log.info( " Tentative de mise à jour de l'étudiant avec l'id: {}",id);
        Student existStudent = personRepository.findStudentById(id)
                .orElseThrow(()->{
                log.warn("Mise à jour a échouée, étudiant non trouvé avec ID: {}",id);
                return new ResourceNotFoundException("Etudiant non trouvé avec id: " + id);
                });
        existStudent.setFirstName(studentDto.getFirstName());
        existStudent.setLastName(studentDto.getLastName());
        existStudent.setEmail(studentDto.getEmail());
        existStudent.setAddress(studentDto.getAddress());
        existStudent.setTelephone(studentDto.getTelephone());
        existStudent.setBirthday(studentDto.getBirthday());
        existStudent.setGender(studentDto.getGender());

        Student updateStudent = personRepository.save(existStudent);
        log.info("Mise à jour réussit de l'étudiant avec id : {}", id);
        return studentMapper.toDto(updateStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {

        // On essaie de trouver l'entité. Si elle n'existe pas,
        // orElseThrow lèvera directement l'exception.

        Student studentToDelete = personRepository.findStudentById(id)
                .orElseThrow(() -> {
                    log.warn("Suppression échouée. étudiant non trouvé avec ID: {}", id);
                    return new ResourceNotFoundException("Impossible de supprimer. étudiant non trouvé avec l'identifiant : " + id);
                });

        // Si on arrive ici, l'étudiant existe. On peut le supprimer.
        personRepository.delete(studentToDelete);

        log.info("Suppression avec succès de l'étudiant avec ID: {}", id);
    }


    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getStudentsByIds(List<Long> ids) {
        log.info("Récupération des étudiants pour les IDs : {}", ids);
        return studentMapper.toDtoList(personRepository.findStudentsByIds(ids));
    }

    @Override
    public StudentDto getStudentByMatricule(String matricule) {
        return  studentMapper.toDto(this.personRepository.findStudentByMatricule(matricule));
    }
}
