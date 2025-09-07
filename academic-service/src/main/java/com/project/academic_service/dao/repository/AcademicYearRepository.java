package com.project.academic_service.dao.repository;

import com.project.academic_service.domain.AcademicYear;
import com.project.academic_service.dto.AcademicYearRestDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Integer> {
    boolean existsByLabel(String label);

    @Query("SELECT ay FROM AcademicYear ay LEFT JOIN FETCH  ay.periods")
    List<AcademicYear> findAllWithPeriods();

    @Query("SELECT ay FROM AcademicYear ay LEFT JOIN FETCH  ay.periods WHERE ay.id = :id")
    Optional<AcademicYear> findByIdWithPeriods(@Param("id") Integer id);

    @Query("SELECT ay FROM AcademicYear ay JOIN FETCH ay.periods WHERE ay.label = :label")
    Optional<AcademicYear> findAcademicYearByLabel(String label);
}