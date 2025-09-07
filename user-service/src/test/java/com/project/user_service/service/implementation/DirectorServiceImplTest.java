package com.project.user_service.service.implementation;

import com.project.user_service.models.dto.DirectorDto;
import com.project.user_service.models.entities.Director;
import com.project.user_service.repository.PersonRepository;
import com.project.user_service.service.mapper.DirectorMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests Unitaires pour DirectorServiceImpl")
class DirectorServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private DirectorMapper directorMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DirectorServiceImpl directorService;

    @Test
    @DisplayName("devrait créer un directeur et hacher son mot de passe")
    void createDirector_shouldSucceedAndHashPassword() {
        // Arrange
        DirectorDto inputDto = new DirectorDto();
        inputDto.setPassword("securePassword");

        Director directorToSave = new Director();
        Director savedDirector = new Director();
        savedDirector.setId(1L);

        when(directorMapper.toEntity(inputDto)).thenReturn(directorToSave);
        when(passwordEncoder.encode("securePassword")).thenReturn("hashedPasswordForDirector");
        when(personRepository.save(any(Director.class))).thenReturn(savedDirector);
        when(directorMapper.toDto(savedDirector)).thenReturn(new DirectorDto());

        // Act
        directorService.createDirector(inputDto);

        // Assert
        ArgumentCaptor<Director> directorCaptor = ArgumentCaptor.forClass(Director.class);
        verify(personRepository).save(directorCaptor.capture());

        Director capturedDirector = directorCaptor.getValue();
        assertThat(capturedDirector.getPassword()).isEqualTo("hashedPasswordForDirector");
    }


}