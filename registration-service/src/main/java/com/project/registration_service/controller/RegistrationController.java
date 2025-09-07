package com.project.registration_service.controller;

import com.project.registration_service.dto.*;
import com.project.registration_service.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

//    @GetMapping
//    public ResponseEntity<List<RegistrationDTO>> getAllRegistrations(){
//        return ResponseEntity.ok(this.registrationService.getAllRegistrations());
//    }

    @GetMapping
    public ResponseEntity<List<RegDTO>> getAllRegs(){
        return ResponseEntity.ok(this.registrationService.getAllRegs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegDTO> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(registrationService.getById(id));
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<RegistrationDTO> getRegistration(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.registrationService.getRegistration(id));
    }*/


    @PutMapping("/{id}")
    public ResponseEntity<RegistrationDTO> update(@PathVariable Long id, @RequestBody RegistrationDTO registrationDTO){
        return ResponseEntity.ok(this.registrationService.updateRegistration(id,registrationDTO));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<RegDTO> updateReg(@PathVariable Long id, @RequestBody UpdateRegDTO updateRegDTO){
        return ResponseEntity.ok(this.registrationService.updateRegs(id,updateRegDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.registrationService.deleteRegistration(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<RegDTO> post(@Validated @RequestBody CreateRegistrationDTO createRegistrationDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.registrationService.processRegistration(createRegistrationDTO));
//        return ResponseEntity.ok(this.registrationService.processRegistration(createRegistrationDTO));
    }

    @GetMapping("/by-student/{matricule}")
    public ResponseEntity<List<RegDTO>> getRegByMatricule(@Validated @PathVariable String matricule){
        return ResponseEntity.ok(this.registrationService.getRegistrationListByMatricule(matricule));
    }

//    @PostMapping
//    public ResponseEntity<RegistrationStudentDTO> create(@RequestBody CreateRegistrationDTO dto) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.create(dto));
//    }
}
