package com.project.registration_service.feign;

import com.project.registration_service.model.AcademicYear;
import com.project.registration_service.model.Period;
import com.project.registration_service.model.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "academic-service")
public interface AcademicRestClient {
    @GetMapping("/api/academic-year/by-label/{label}")
    AcademicYear getAcademicYearByLabel(@PathVariable String label);

    @GetMapping("/api/period/periods")
    List<Period> getAllPeriods();
}
