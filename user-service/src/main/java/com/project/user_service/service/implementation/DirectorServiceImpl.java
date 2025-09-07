package com.project.user_service.service.implementation;

import com.project.user_service.exception.EmailAlreadyExistsException;
import com.project.user_service.exception.ResourceNotFoundException;
import com.project.user_service.models.dto.DirectorDto;

import com.project.user_service.models.entities.Director;
import com.project.user_service.repository.PersonRepository;
import com.project.user_service.service.common.IDirectorService;
import com.project.user_service.service.mapper.DirectorMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements IDirectorService {
    private static final Logger log = LogManager.getLogger(DirectorServiceImpl.class);

    private final PersonRepository personRepository;
    private final DirectorMapper directorMapper;
    private final PasswordEncoder passwordEncoder; // <-- Injection du bean de hachage

    @Override
    @Transactional
    public DirectorDto createDirector(DirectorDto dto) {
        log.info("Tentative de création d'un Director avec email: {}", dto.getEmail());

        Director director = directorMapper.toEntity(dto);
        if (personRepository.findByEmail(director.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("L'email " + director.getEmail() + " est déjà utilisé par un autre utilisateur");
        }
        // --- GESTION DE LA SÉCURITÉ ---
        // On hache le mot de passe fourni dans le DTO avant de le sauvegarder en base.
        // On ne sauvegarde JAMAIS un mot de passe en clair.
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            director.setPassword(passwordEncoder.encode(dto.getPassword()));
            log.debug("Mot de passe haché pour l'utilisateur {}", dto.getEmail());
        }

        Director savedDirector = personRepository.save(director);
        log.info("Director créé avec succès. ID: {}", savedDirector.getId());

        return directorMapper.toDto(savedDirector);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DirectorDto> getAllDirectors() {
        log.info("Récupération de tous les Directors.");
        return personRepository.findAllDirectors().stream()
                .map(directorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DirectorDto getDirectorById(Long id) {
        log.info("Recherche du Director avec ID: {}", id);
        return personRepository.findDirectorById(id)
                .map(directorMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Director non trouvé avec ID: {}", id);
                    return new ResourceNotFoundException("Director non trouvé avec l'ID : " + id);
                });
    }

    @Override
    @Transactional
    public DirectorDto updateDirector(Long id, DirectorDto dto) {
        log.info("Tentative de mise à jour du Director avec ID: {}", id);

        Director existingManager = personRepository.findDirectorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director non trouvé avec l'ID : " + id));

        // Mettre à jour les champs de base (on ne met pas à jour le mot de passe ici par défaut)
        existingManager.setFirstName(dto.getFirstName());
        existingManager.setLastName(dto.getLastName());
        existingManager.setEmail(dto.getEmail());
        existingManager.setAddress(dto.getAddress());
        existingManager.setTelephone(dto.getTelephone());
        existingManager.setBirthday(dto.getBirthday());
        existingManager.setGender(dto.getGender());

        Director updatedManager = personRepository.save(existingManager);
        log.info("Mise à jour réussie pour Director ID: {}", id);
        return directorMapper.toDto(updatedManager);
    }

    @Override
    @Transactional
    public void deleteDirector(Long id) {
        log.info("Tentative de suppression du Director avec ID: {}", id);
        if (!personRepository.existsById(id)) {
            log.warn("Échec de la suppression. Director non trouvé avec ID: {}", id);
            throw new ResourceNotFoundException("Director non trouvé avec l'ID : " + id);
        }
        personRepository.deleteById(id);
        log.info("Director supprimé avec succès. ID: {}", id);
    }
}
