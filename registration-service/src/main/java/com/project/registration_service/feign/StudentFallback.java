package com.project.registration_service.feign;

import com.project.registration_service.model.Student;
import org.springframework.hateoas.PagedModel;

import java.util.Date;

public class StudentFallback implements StudentRestClient{
    @Override
    public Student getStudentById(Long id) {
        return null;
    }

    // this implementation Will be changed in order to consider the cache memory
    @Override
    public Student getStudentByMatricule(String matricule) {
        Student student = Student.builder()
                .email("default@gmail.com")
                .address("16 rue des Loges")
                .gender("Masculin")
                .lastName("Doe")
                .firstName("John")
                .telephone("+33748965787")
                .birthday(new Date(12/4/2002))
                .matricule("ENS-003")
                .build();
        return student;
    }

    @Override
    public PagedModel<Student> findAllStudents() {
        return null;
    }
}
