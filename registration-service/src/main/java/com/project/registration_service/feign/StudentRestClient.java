package com.project.registration_service.feign;

import com.project.registration_service.model.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", fallback = StudentFallback.class)
public interface StudentRestClient {

    @GetMapping("/api/students/{id}")
    Student getStudentById(@PathVariable Long id);

    @GetMapping("/api/students/by-matricule/{matricule}")
    Student getStudentByMatricule(@PathVariable String matricule);

    @GetMapping("/api/students/by-ids")
    PagedModel<Student> findAllStudents();

}
