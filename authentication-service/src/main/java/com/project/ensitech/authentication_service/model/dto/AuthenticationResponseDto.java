package com.project.ensitech.authentication_service.model.dto;

import com.project.ensitech.authentication_service.model.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {
    private Integer id;
    private String email;
    private Role role;
    private String token;
}
