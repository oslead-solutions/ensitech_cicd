package com.project.ensitech.training_service.client;

import com.project.ensitech.training_service.model.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** Simple “exists” endpoints (returns 200 or 404) */
@FeignClient(name="studentClient", url="${user.service.base-url}")
public interface StudentClient {
    @GetMapping("/api/students/{id}")
    UserDto getStudent(@PathVariable Long id);
}
