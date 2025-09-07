package com.project.user_service.service.implementation;

import com.project.user_service.exception.ResourceNotFoundException;
import com.project.user_service.models.dto.TeacherDto;
import com.project.user_service.models.entities.Teacher;
import com.project.user_service.repository.PersonRepository;
import com.project.user_service.service.mapper.TeacherMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests Unitaires pour TeacherServiceImpl")
class TeacherServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Albert");
        teacher.setLastName("Einstein");
        teacher.setEmail("albert.e@ensitech.com");

        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("Albert");
        teacherDto.setLastName("Einstein");
        teacherDto.setEmail("albert.e@ensitech.com");
    }

    // --- Tests pour createTeacher ---
    @Test
    @DisplayName("devrait créer un enseignant et initialiser createdAt")
    void createTeacher_shouldSucceedAndSetCreationDate() {
        // Arrange
        TeacherDto inputDto = new TeacherDto();
        Teacher teacherToSave = new Teacher();

        when(teacherMapper.toEntity(inputDto)).thenReturn(teacherToSave);
        when(personRepository.save(any(Teacher.class))).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        // Act
        teacherService.createTeacher(inputDto);

        // Assert
        // ArgumentCaptor permet d'inspecter l'objet qui a été passé à la méthode save
        ArgumentCaptor<Teacher> teacherArgumentCaptor = ArgumentCaptor.forClass(Teacher.class);
        verify(personRepository).save(teacherArgumentCaptor.capture());

        Teacher capturedTeacher = teacherArgumentCaptor.getValue();
        assertThat(capturedTeacher.getCreatedAt()).isNotNull();
    }

    // --- Tests pour getAllTeachers ---
    @Test
    @DisplayName("devrait retourner une collection de tous les enseignants")
    void getAllTeachers_shouldReturnTeacherCollection() {
        // Arrange
        when(personRepository.findAllTeachers()).thenReturn(List.of(teacher));
        when(teacherMapper.toDtoList(anyList())).thenReturn(List.of(teacherDto));

        // Act
        Collection<TeacherDto> result = teacherService.getAllTeachers();

        // Assert
        assertThat(result).isNotNull().hasSize(1);
    }

    // --- Tests pour getTeacherById ---
    @Test
    @DisplayName("devrait lancer ResourceNotFoundException si l'enseignant n'est pas trouvé par ID")
    void getTeacherById_whenNotFound_shouldThrowException() {
        // Arrange
        when(personRepository.findTeacherById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> teacherService.getTeacherById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- Tests pour updateTeacher ---
    @Test
    @DisplayName("devrait mettre à jour un enseignant avec succès")
    void updateTeacher_shouldSucceed() {
        // Arrange
        TeacherDto updateDto = new TeacherDto();
        updateDto.setFirstName("Alberto");

        when(personRepository.findTeacherById(1L)).thenReturn(Optional.of(teacher));
        when(personRepository.save(any(Teacher.class))).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        // Act
        teacherService.updateTeacher(1L, updateDto);

        // Assert
        ArgumentCaptor<Teacher> captor = ArgumentCaptor.forClass(Teacher.class);
        verify(personRepository).save(captor.capture());
        assertThat(captor.getValue().getFirstName()).isEqualTo("Alberto");
    }

    // --- Tests pour deleteTeacher ---
    @Test
    @DisplayName("devrait appeler la méthode delete du repository si l'enseignant existe")
    void deleteTeacher_whenFound_shouldCallDelete() {
        // Arrange
        when(personRepository.findTeacherById(1L)).thenReturn(Optional.of(teacher));

        // Act
        teacherService.deleteTeacher(1L);

        // Assert
        verify(personRepository, times(1)).delete(teacher);
    }
}