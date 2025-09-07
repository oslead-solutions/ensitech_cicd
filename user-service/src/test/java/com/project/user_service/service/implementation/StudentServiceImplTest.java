package com.project.user_service.service.implementation;

import com.project.user_service.exception.ResourceNotFoundException;
import com.project.user_service.models.dto.StudentDto;
import com.project.user_service.models.entities.Student;
import com.project.user_service.repository.PersonRepository;
import com.project.user_service.service.mapper.StudentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests Unitaires pour StudentServiceImpl")
class StudentServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentDto studentDto;

    @BeforeEach
    void setUp() {
        // Initialisation des objets de test communs
        student = new Student();
        student.setId(1L);
        student.setFirstName("Sidonie");
        student.setLastName("Curie");
        student.setEmail("Sidonie.c@ensitech.com");
        student.setMatricule("ENS-MC001");

        studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setFirstName("Sidonie");
        studentDto.setLastName("Curie");
        studentDto.setEmail("Sidonie.c@ensitech.com");
        studentDto.setMatricule("ENS-MC001");
    }

    // --- Tests pour createStudent ---
    @Test
    @DisplayName("devrait créer un étudiant avec succès et générer un matricule")
    void createStudent_shouldSucceedAndGenerateMatricule() {
        // Arrange
        StudentDto inputDto = new StudentDto(); // DTO sans ID ni matricule
        inputDto.setEmail("test@email.com");

        Student studentToSave = new Student();

        when(studentMapper.toEntity(inputDto)).thenReturn(studentToSave);
        when(personRepository.save(any(Student.class))).thenReturn(student); // Retourne l'étudiant avec ID
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        // Act
        StudentDto resultDto = studentService.createStudent(inputDto);

        // Assert
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(1L);
        assertThat(studentToSave.getMatricule()).isNotNull().startsWith("ENS-");
        verify(personRepository, times(1)).save(studentToSave);
    }

    // --- Tests pour getAllStudents ---
    @Test
    @DisplayName("devrait retourner une liste de tous les étudiants")
    void getAllStudents_shouldReturnStudentList() {
        // Arrange
        when(personRepository.findAllStudents()).thenReturn(Collections.singletonList(student));
        when(studentMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(studentDto));

        // Act
        List<StudentDto> resultList = studentService.getAllStudents();

        // Assert
        assertThat(resultList).isNotNull().hasSize(1);
        assertThat(resultList.get(0).getId()).isEqualTo(student.getId());
        verify(personRepository, times(1)).findAllStudents();
    }

    @Test
    @DisplayName("devrait retourner une liste vide si aucun étudiant n'est trouvé")
    void getAllStudents_whenNoneFound_shouldReturnEmptyList() {
        // Arrange
        when(personRepository.findAllStudents()).thenReturn(Collections.emptyList());

        // Act
        List<StudentDto> resultList = studentService.getAllStudents();

        // Assert
        assertThat(resultList).isNotNull().isEmpty();
    }

    // --- Tests pour getStudentById ---
    @Test
    @DisplayName("devrait retourner un étudiant par son ID")
    void getStudentById_whenFound_shouldReturnStudent() {
        // Arrange
        when(personRepository.findStudentById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        // Act
        StudentDto resultDto = studentService.getStudentById(1L);

        // Assert
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("devrait lancer ResourceNotFoundException si l'étudiant n'est pas trouvé par ID")
    void getStudentById_whenNotFound_shouldThrowException() {
        // Arrange
        when(personRepository.findStudentById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Étudiant non trouvé");
    }

    // --- Tests pour updateStudent ---
    @Test
    @DisplayName("devrait mettre à jour un étudiant avec succès")
    void updateStudent_shouldSucceed() {
        // Arrange
        StudentDto updateDataDto = new StudentDto();
        updateDataDto.setFirstName("Maria");

        when(personRepository.findStudentById(1L)).thenReturn(Optional.of(student));
        when(personRepository.save(any(Student.class))).thenReturn(student); // Le save retourne l'objet mis à jour
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        // Act
        studentService.updateStudent(1L, updateDataDto);

        // Assert
        verify(personRepository).save(any(Student.class));
        assertThat(student.getFirstName()).isEqualTo("Maria");
    }

    @Test
    @DisplayName("devrait lancer ResourceNotFoundException lors de la mise à jour si l'étudiant n'existe pas")
    void updateStudent_whenNotFound_shouldThrowException() {
        // Arrange
        when(personRepository.findStudentById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> studentService.updateStudent(99L, new StudentDto()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- Tests pour deleteStudent ---
    @Test
    @DisplayName("devrait supprimer un étudiant avec succès")
    void deleteStudent_shouldSucceed() {
        // Arrange
        when(personRepository.findStudentById(1L)).thenReturn(Optional.of(student));
        doNothing().when(personRepository).delete(student);

        // Act
        studentService.deleteStudent(1L);

        // Assert
        verify(personRepository, times(1)).delete(student);
    }

    @Test
    @DisplayName("devrait lancer ResourceNotFoundException lors de la suppression si l'étudiant n'existe pas")
    void deleteStudent_whenNotFound_shouldThrowException() {
        // Arrange
        when(personRepository.findStudentById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> studentService.deleteStudent(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // --- Tests pour getStudentsByIds ---
    @Test
    @DisplayName("devrait retourner une liste d'étudiants pour une liste d'IDs")
    void getStudentsByIds_shouldReturnStudentList() {
        // Arrange
        List<Long> ids = List.of(1L);
        when(personRepository.findStudentsByIds(ids)).thenReturn(List.of(student));
        when(studentMapper.toDtoList(anyList())).thenReturn(List.of(studentDto));

        // Act
        List<StudentDto> result = studentService.getStudentsByIds(ids);

        // Assert
        assertThat(result).isNotNull().hasSize(1);
    }
}