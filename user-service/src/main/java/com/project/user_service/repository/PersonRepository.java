package com.project.user_service.repository;

import com.project.user_service.models.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


@Repository // Indique à Spring que c'est un composant de la couche d'accès aux données
public interface PersonRepository extends JpaRepository <Person, Long>{
    // Spring Data JPA est magique. En déclarant cette méthode, il sait automatiquement
    // comment chercher une Person par son email, car 'email' est un champ de l'entité Person.

    Optional<Person> findByEmail(String email);

//    Optional<Person> findByfirstName(String firstName);

    @Query("SELECT t FROM Teacher t WHERE t.id = :id")
    Optional<Teacher> findTeacherById(@Param("id")Long id);

    // Pour obtenir UNIQUEMENT les enseignants, nous pouvons utiliser une requête JPQL.
    // 'SELECT t FROM Teacher t' est très explicite : "Sélectionne les entités t où t est de type Teacher".
    // Hibernate saura traduire cela en une requête SQL avec "WHERE person_type = 1".
    @Query("SELECT t FROM Teacher t")
    List<Teacher> findAllTeachers();

//    @Query("SELECT t FROM Teacher t WHERE t.id IN ids")
//    List<Teacher> findTeachersByIds(@Param("ids")Long ids);
//

    // Pour les Étudiants
    @Query("SELECT s FROM Student s")
    List<Student> findAllStudents();

    @Query("SELECT s FROM Student s WHERE s.id = :id")
    Optional<Student> findStudentById(@Param("id")Long id);


    @Query("SELECT s FROM Student s WHERE s.id IN :ids")
    List<Student> findStudentsByIds(@Param("ids") List<Long> ids);

    // Pour les StudyManagers
    @Query("SELECT sm FROM StudyManager sm")
    List<StudyManager> findAllStudyManagers();

    @Query("SELECT sm FROM StudyManager sm WHERE sm.id = :id")
    Optional<StudyManager> findStudyManagerById(Long id);

    // Pour les Directors
    @Query("SELECT d FROM Director d")
    List<Director> findAllDirectors();

    @Query("SELECT d FROM Director d WHERE d.id = :id")
    Optional<Director> findDirectorById(Long id);

    // Pour les Administrators
    @Query("SELECT a FROM Administrator a")
    List<Administrator> findAllAdministrators();

    @Query("SELECT a FROM Administrator a WHERE a.id = :id")
    Optional<Administrator> findAdministratorById(Long id);

    @Query("SELECT s FROM Student s WHERE s.matricule = :matricule")
    Student findStudentByMatricule(@Param("matricule") String matricule);

}
