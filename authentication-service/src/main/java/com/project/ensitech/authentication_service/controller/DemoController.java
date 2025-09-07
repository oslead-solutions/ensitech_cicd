package com.project.ensitech.authentication_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth/demo")
public class DemoController {
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok ("Hello,  this endpoint is secured");
    }
    @GetMapping("/test-error")
    public ResponseEntity<?> testError() {
        throw new RuntimeException("Erreur simulée");
    }
}
