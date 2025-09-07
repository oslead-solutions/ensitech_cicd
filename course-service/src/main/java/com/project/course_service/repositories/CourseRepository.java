package com.project.course_service.repositories;


import com.project.course_service.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
//    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.teacher WHERE c.id = :id")
//    Optional<Course> findByIdWithTeacher(@Param("id") Long id);
//
//    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.teacher")
//    List<Course> findAllWithTeacher();

  /* @param title Le titre du cours à vérifier.
     * @return true si un cours avec ce titre existe, false sinon.
     */
    boolean existsByTitle(String title);
}