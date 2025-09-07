package com.project.user_service.service.implementation;

import com.project.user_service.exception.EmailAlreadyExistsException;
import com.project.user_service.exception.ResourceNotFoundException;
import com.project.user_service.models.dto.AdministratorDto;

import com.project.user_service.models.entities.Administrator;
import com.project.user_service.repository.PersonRepository;
import com.project.user_service.service.common.IAdministratorService;
import com.project.user_service.service.mapper.AdministratorMapper;
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
public class AdministratorServiceImpl implements IAdministratorService {
    private static final Logger log = LogManager.getLogger(AdministratorServiceImpl.class);

    private final PersonRepository personRepository;
    private final AdministratorMapper administratorMapper;
    private final PasswordEncoder passwordEncoder; // <-- Injection du bean de hachage

    @Override
    @Transactional
    public AdministratorDto createAdministrator(AdministratorDto dto) {
        log.info("Tentative de création d'un Administrator avec email: {}", dto.getEmail());

        Administrator administrator = administratorMapper.toEntity(dto);
        if (personRepository.findByEmail(administrator.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("L'email " + administrator.getEmail() + " est déjà utilisé par un autre utilisateur");
        }
        // --- GESTION DE LA SÉCURITÉ ---
        // On hache le mot de passe fourni dans le DTO avant de le sauvegarder en base.
        // On ne sauvegarde JAMAIS un mot de passe en clair.
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            administrator.setPassword(passwordEncoder.encode(dto.getPassword()));
            log.debug("Mot de passe haché pour l'utilisateur {}", dto.getEmail());
        }

        Administrator savedAdministrator = personRepository.save( administrator);
        log.info("Administrator créé avec succès. ID: {}", savedAdministrator.getId());

        return administratorMapper.toDto(savedAdministrator);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdministratorDto> getAllAdministrators() {
        log.info("Récupération de tous les Administrators.");
        return personRepository.findAllAdministrators().stream()
                .map(administratorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdministratorDto getAdministratorById(Long id) {
        log.info("Recherche du Administrator avec ID: {}", id);
        return personRepository.findAdministratorById(id)
                .map(administratorMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Administrator non trouvé avec ID: {}", id);
                    return new ResourceNotFoundException("Administrator non trouvé avec l'ID : " + id);
                });
    }

    @Override
    @Transactional
    public AdministratorDto updateAdministrator(Long id, AdministratorDto dto) {
        log.info("Tentative de mise à jour du Administrator avec ID: {}", id);

        Administrator existingManager = personRepository.findAdministratorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrator non trouvé avec l'ID : " + id));

        // Mettre à jour les champs de base (on ne met pas à jour le mot de passe ici par défaut)
        existingManager.setFirstName(dto.getFirstName());
        existingManager.setLastName(dto.getLastName());
        existingManager.setEmail(dto.getEmail());
        existingManager.setAddress(dto.getAddress());
        existingManager.setTelephone(dto.getTelephone());
        existingManager.setBirthday(dto.getBirthday());
        existingManager.setGender(dto.getGender());

        Administrator updatedManager = personRepository.save(existingManager);
        log.info("Mise à jour réussie pour Administrator ID: {}", id);
        return administratorMapper.toDto(updatedManager);
    }

    @Override
    @Transactional
    public void deleteAdministrator(Long id) {
        log.info("Tentative de suppression du Administrator avec ID: {}", id);
        if (!personRepository.existsById(id)) {
            log.warn("Échec de la suppression. Administrator non trouvé avec ID: {}", id);
            throw new ResourceNotFoundException("Administrator non trouvé avec l'ID : " + id);
        }
        personRepository.deleteById(id);
        log.info("Administrator supprimé avec succès. ID: {}", id);
    }
}
