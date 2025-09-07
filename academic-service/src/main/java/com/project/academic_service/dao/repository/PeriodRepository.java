package com.project.academic_service.dao.repository;

import com.project.academic_service.domain.Period;
import com.project.academic_service.enumeration.TypePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Integer> {
    boolean existsByStartedAtAndEndedAt(LocalDate start,LocalDate end);
    boolean existsByEntitled(String entitled);

}
