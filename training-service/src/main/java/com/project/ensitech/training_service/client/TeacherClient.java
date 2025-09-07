package com.project.ensitech.training_service.client;

import com.project.ensitech.training_service.model.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="teacherClient", url="${user.service.base-url}")
public interface TeacherClient {
    @GetMapping("/api/teachers/{id}")
    UserDto getTeacher(@PathVariable Long id);
}
