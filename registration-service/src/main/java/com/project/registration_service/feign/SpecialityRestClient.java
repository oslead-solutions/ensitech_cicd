package com.project.registration_service.feign;

import com.project.registration_service.model.Speciality;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "training-service")
public interface SpecialityRestClient {

    @GetMapping("/api/training/specialities/by-speciality/{label}")
    Speciality getSpecialityByLabel(@PathVariable String label);
    
}
