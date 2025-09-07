package com.project.ensitech.training_service.repository;

import com.project.ensitech.training_service.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTitleContainingIgnoreCase(String title);
    List<Course> findByTeacherId(Long teacherId);
}
