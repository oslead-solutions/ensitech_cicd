package com.project.registration_service.dao.repository;

import com.project.registration_service.domain.Registration;
import com.project.registration_service.dto.RegistrationStudentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration , Long> {
    boolean existsByRegistrationNumber(Long number);
    List<Registration> findByMatricule(String matricule);
    
    Optional<Registration> findByMatriculeAndAcademicYearLabel(String matricule, String label);
    List<Registration> findByAcademicYearLabel(String academicYearLabel);
}
