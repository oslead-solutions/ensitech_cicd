package com.project.course_service.service.implementation;

import com.project.course_service.entities.Course;
import com.project.course_service.entities.dto.CourseDto;
import com.project.course_service.entities.dto.Teacher;
import com.project.course_service.exception.ResourceNotFoundException;
import com.project.course_service.feign.UserRestClient;
import com.project.course_service.repositories.CourseRepository;
import com.project.course_service.service.mappeur.CourseMapper;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    // --- ARRANGE (Commun à tous les tests) ---
    // On déclare nos dépendances comme des Mocks (des doublures)
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserRestClient userRestClient;
    @Mock
    private CourseMapper courseMapper;

    // On injecte ces Mocks dans une VRAIE instance du service que l'on veut tester
    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private Long courseId = 1L;
    private Long teacherId = 100L;

    @BeforeEach
    void setUp() {
        course = Course.builder().id(courseId).title("Maths").build();
    }



    @Test
    void createCourseShouldRetourCourseDao() {

    }

    @Test
    void updateCourse() {
    }

    @Test
    void getCourseById() {
    }

    @Test
    void getAllCourses() {
    }

    @Test
    void deleteCourse() {
    }

    // --- Test pour le Scénario 1 : Cas Nominal ---
    // Dans CourseServiceImplTest.java

    @Test
    @DisplayName("Devrait assigner un enseignant avec succès")
    void assignTeacherToCourse_whenCourseAndTeacherExist_shouldSucceed() {
        // --- ARRANGE ---


        Course courseFromDb = Course.builder().id(courseId).title("Algorithmique").build();
        Course savedCourse = Course.builder().id(courseId).title("Algorithmique").teacherId(teacherId).build();
        CourseDto expectedDto = new CourseDto();

        // Données pour l'enrichissement
        Teacher teacherFromFeign = new Teacher();
        teacherFromFeign.setId(teacherId);
        teacherFromFeign.setFirstName("Albert");

        // --- Configuration des Mocks (Mise à jour) ---

        // 1. Pour la recherche initiale du cours
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseFromDb));

        // 2. Pour la validation de l'enseignant
        when(userRestClient.getTeacherById(teacherId)).thenReturn(new Teacher()); // On retourne un objet Teacher factice

        // 3. Pour la sauvegarde du cours
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        // 4. Pour le mapping de base
        when(courseMapper.toDto(savedCourse)).thenReturn(expectedDto);

        // 5. NOUVEAU : Pour l'appel d'enrichissement à la fin de la méthode
        when(userRestClient.getTeacherById(teacherId)).thenReturn(teacherFromFeign);

        // --- ACT ---
        CourseDto resultDto = courseService.assignTeacherToCourse(courseId, teacherId);

        // --- ASSERT ---
        assertThat(resultDto).isNotNull();

        // On peut même vérifier que le DTO a été enrichi
        assertThat(resultDto.getTeacher()).isNotNull();
        assertThat(resultDto.getTeacher().getId()).isEqualTo(teacherId);

        assertThat(courseFromDb.getTeacherId()).isEqualTo(teacherId);
        verify(courseRepository).save(courseFromDb);
    }

    @Test
    @DisplayName("Devrait lancer ResourceNotFoundException si le cours n'existe pas")
    void assignTeacherToCourse_whenCourseNotFound_shouldThrowResourceNotFoundException() {
        // Arrange
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> courseService.assignTeacherToCourse(courseId, teacherId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cours non trouvé avec l'ID : " + courseId);

        verifyNoInteractions(userRestClient);
        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Devrait lancer ResourceNotFoundException si l'enseignant n'existe pas")
    void assignTeacherToCourse_whenTeacherNotFound_shouldThrowResourceNotFoundException() {
        // Arrange
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        // On simule une erreur Feign (ex: un 404 de user-service)
        when(userRestClient.getTeacherById(teacherId)).thenThrow(FeignException.NotFound.class);

        // Act & Assert
        assertThatThrownBy(() -> courseService.assignTeacherToCourse(courseId, teacherId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Validation échouée : Enseignant non trouvé avec l'ID : " + teacherId);

        verify(courseRepository, never()).save(any());
    }

    @Test
    void enrollStudentInCourse() {
    }

    @Test
    void removeStudentFromCourse() {
    }
}