package com.project.course_service.feign;

import com.project.course_service.entities.dto.Student;
import com.project.course_service.entities.dto.Teacher;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// Le nom "user-service" doit correspondre à celui dans Eureka.
@FeignClient(name = "user-service")
public interface UserRestClient {
    @GetMapping("/api/students/{id}")
    Student getStudentById(@PathVariable Long id);

    @GetMapping("/api/teachers/{id}")
    Teacher getTeacherById(@PathVariable Long id);

    @GetMapping("/api/students/by-ids")
    List<Student> getStudentsByIds(@RequestParam List<Long> ids);

    @GetMapping("/api/teachers/by-ids")
    List<Teacher> getTeachersByIds(@RequestParam List<Long> ids);

    // Si vous avez besoin de récupérer plusieurs étudiants en une fois
}
