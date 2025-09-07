package com.project.ensitech.authentication_service.controller;

import com.project.ensitech.authentication_service.model.dto.*;
import com.project.ensitech.authentication_service.service.common.IAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthentificationController {
private final IAuthenticationService iAuthenticationService;
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/register")
    //public ResponseEntity<AuthenticationResponse> register(
    public ResponseEntity<String> register(
            @Valid
            @RequestBody RegisterRequestDto request
    ) {
      return ResponseEntity.ok(iAuthenticationService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(
            @Valid
            @RequestBody LoginRequestDto request
    ) {
        return ResponseEntity.ok(iAuthenticationService.login(request));
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordRequestDto request) {
        return ResponseEntity.ok(iAuthenticationService.updatePassword( request));

    }

    @PutMapping("/update-email/{id}")
    public ResponseEntity<String> updateEmail(@Valid @PathVariable Integer id, @RequestBody UpdateEmailRequestDto request) {
        return ResponseEntity.ok(iAuthenticationService.updateEmail(id, request));
    }

}
