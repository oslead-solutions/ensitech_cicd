package com.project.user_service.service.implementation;

import com.project.user_service.exception.EmailAlreadyExistsException;
import com.project.user_service.exception.ResourceNotFoundException;
import com.project.user_service.models.dto.StudyManagerDto;
import com.project.user_service.models.entities.StudyManager;
import com.project.user_service.repository.PersonRepository;
import com.project.user_service.service.common.IStudyManagerService;
import com.project.user_service.service.mapper.StudyManagerMapper;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyManagerServiceImpl implements IStudyManagerService {
    private static final Logger log = LogManager.getLogger(StudyManagerServiceImpl.class);

    private final PersonRepository personRepository;
    private final StudyManagerMapper studyManagerMapper;
    private final PasswordEncoder passwordEncoder; // <-- Injection du bean de hachage

    @Override
    @Transactional
    public StudyManagerDto createStudyManager(StudyManagerDto dto) {
        log.info("Tentative de création d'un StudyManager avec email: {}", dto.getEmail());

        StudyManager studyManager = studyManagerMapper.toEntity(dto);
        if (personRepository.findByEmail(studyManager.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("L'email " + studyManager.getEmail() + " est déjà utilisé par un autre utilisateur");
        }
        // --- GESTION DE LA SÉCURITÉ ---
        // On hache le mot de passe fourni dans le DTO avant de le sauvegarder en base.
        // On ne sauvegarde JAMAIS un mot de passe en clair.
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            studyManager.setPassword(passwordEncoder.encode(dto.getPassword()));
            log.debug("Mot de passe haché pour l'utilisateur {}", dto.getEmail());
        }

        StudyManager savedStudyManager = personRepository.save(studyManager);
        log.info("StudyManager créé avec succès. ID: {}", savedStudyManager.getId());

        return studyManagerMapper.toDto(savedStudyManager);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudyManagerDto> getAllStudyManagers() {
        log.info("Récupération de tous les StudyManagers.");
        return personRepository.findAllStudyManagers().stream()
                .map(studyManagerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudyManagerDto getStudyManagerById(Long id) {
        log.info("Recherche du StudyManager avec ID: {}", id);
        return personRepository.findStudyManagerById(id)
                .map(studyManagerMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("StudyManager non trouvé avec ID: {}", id);
                    return new ResourceNotFoundException("StudyManager non trouvé avec l'ID : " + id);
                });
    }

    @Override
    @Transactional
    public StudyManagerDto updateStudyManager(Long id, StudyManagerDto dto) {
        log.info("Tentative de mise à jour du StudyManager avec ID: {}", id);

        StudyManager existingManager = personRepository.findStudyManagerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StudyManager non trouvé avec l'ID : " + id));

        // Mettre à jour les champs de base (on ne met pas à jour le mot de passe ici par défaut)
        existingManager.setFirstName(dto.getFirstName());
        existingManager.setLastName(dto.getLastName());
        existingManager.setEmail(dto.getEmail());
        existingManager.setAddress(dto.getAddress());
        existingManager.setTelephone(dto.getTelephone());
        existingManager.setBirthday(dto.getBirthday());
        existingManager.setGender(dto.getGender());

        StudyManager updatedManager = personRepository.save(existingManager);
        log.info("Mise à jour réussie pour StudyManager ID: {}", id);
        return studyManagerMapper.toDto(updatedManager);
    }

    @Override
    @Transactional
    public void deleteStudyManager(Long id) {
        log.info("Tentative de suppression du StudyManager avec ID: {}", id);
        if (!personRepository.existsById(id)) {
            log.warn("Échec de la suppression. StudyManager non trouvé avec ID: {}", id);
            throw new ResourceNotFoundException("StudyManager non trouvé avec l'ID : " + id);
        }
        personRepository.deleteById(id);
        log.info("StudyManager supprimé avec succès. ID: {}", id);
    }
}