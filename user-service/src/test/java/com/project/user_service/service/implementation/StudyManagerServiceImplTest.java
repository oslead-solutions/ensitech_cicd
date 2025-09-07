package com.project.user_service.service.implementation;

import com.project.user_service.exception.ResourceNotFoundException;
import com.project.user_service.models.dto.StudyManagerDto;
import com.project.user_service.models.entities.StudyManager;
import com.project.user_service.repository.PersonRepository;
import com.project.user_service.service.mapper.StudyManagerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder; // Importer PasswordEncoder

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests Unitaires pour StudyManagerServiceImpl")
class StudyManagerServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private StudyManagerMapper studyManagerMapper;

    @Mock // NOUVEAU : On mocke le PasswordEncoder
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudyManagerServiceImpl studyManagerService;

    private StudyManager studyManager;
    private StudyManagerDto studyManagerDto;

    @BeforeEach
    void setUp() {
        studyManager = new StudyManager();
        studyManager.setId(1L);
        studyManager.setFirstName("John");
        studyManager.setLastName("Smith");
        studyManager.setEmail("john.smith@ensitech.com");

        studyManagerDto = new StudyManagerDto();
        studyManagerDto.setId(1L);
        studyManagerDto.setFirstName("John");
        studyManagerDto.setLastName("Smith");
        studyManagerDto.setEmail("john.smith@ensitech.com");
    }

    // --- Tests pour createStudyManager ---
    @Test
    @DisplayName("devrait créer un StudyManager et hacher son mot de passe")
    void createStudyManager_shouldSucceedAndHashPassword() {
        // Arrange
        StudyManagerDto inputDto = new StudyManagerDto();
        inputDto.setPassword("plainPassword123"); // Mot de passe en clair

        StudyManager managerToSave = new StudyManager();

        when(studyManagerMapper.toEntity(inputDto)).thenReturn(managerToSave);
        // On configure le mock du PasswordEncoder
        when(passwordEncoder.encode("plainPassword123")).thenReturn("hashedPasswordXYZ");
        when(personRepository.save(any(StudyManager.class))).thenReturn(studyManager);
        when(studyManagerMapper.toDto(studyManager)).thenReturn(studyManagerDto);

        // Act
        studyManagerService.createStudyManager(inputDto);

        // Assert
        // On capture l'objet passé à la méthode save
        ArgumentCaptor<StudyManager> managerCaptor = ArgumentCaptor.forClass(StudyManager.class);
        verify(personRepository).save(managerCaptor.capture());

        StudyManager capturedManager = managerCaptor.getValue();
        // On vérifie que le mot de passe sauvegardé est bien la version hachée
        assertThat(capturedManager.getPassword()).isEqualTo("hashedPasswordXYZ");
        // On vérifie que la méthode encode a bien été appelée
        verify(passwordEncoder, times(1)).encode("plainPassword123");
    }

    @Test
    @DisplayName("ne devrait pas hacher le mot de passe s'il est nul ou vide")
    void createStudyManager_whenPasswordIsNull_shouldNotCallEncoder() {
        // Arrange
        StudyManagerDto inputDtoWithNullPassword = new StudyManagerDto();
        inputDtoWithNullPassword.setPassword(null);

        StudyManager managerToSave = new StudyManager();
        when(studyManagerMapper.toEntity(inputDtoWithNullPassword)).thenReturn(managerToSave);
        when(personRepository.save(any(StudyManager.class))).thenReturn(new StudyManager());

        // Act
        studyManagerService.createStudyManager(inputDtoWithNullPassword);

        // Assert
        // On vérifie que le PasswordEncoder n'a JAMAIS été utilisé
        verifyNoInteractions(passwordEncoder);
        assertThat(managerToSave.getPassword()).isNull();
    }

    // --- Tests pour updateStudyManager ---
    @Test
    @DisplayName("ne devrait pas modifier le mot de passe lors d'une mise à jour standard")
    void updateStudyManager_shouldNotChangePassword() {
        // Arrange
        StudyManagerDto updateDto = new StudyManagerDto();
        updateDto.setFirstName("Jonathan");

        when(personRepository.findStudyManagerById(1L)).thenReturn(Optional.of(studyManager));
        when(personRepository.save(any(StudyManager.class))).thenReturn(studyManager);

        // Act
        studyManagerService.updateStudyManager(1L, updateDto);

        // Assert
        // On vérifie que le service de hachage n'est jamais appelé lors d'un update
        verifyNoInteractions(passwordEncoder);
    }

    //  On peut maintenant tester les méthodes de la classe StudyManagerServiceImpl
    //  ici les tests pour getAllStudyManagers et getStudyManagerById

}