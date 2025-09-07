package com.project.user_service.controller;



import com.project.user_service.models.dto.StudyManagerDto;
import com.project.user_service.service.common.IStudyManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studymanagers") // Endpoint dédié
@RequiredArgsConstructor
public class StudyManagerController {
    private final IStudyManagerService studyManagerService;

    /**
     * Crée un nouveau StudyManager. Le mot de passe sera haché par le service.
     * @param dto Les données du StudyManager à créer.
     * @return Le DTO du StudyManager créé (sans le mot de passe).
     */
    @PostMapping
    public ResponseEntity<StudyManagerDto> createStudyManager(@Valid @RequestBody StudyManagerDto dto) {
        StudyManagerDto createdManager = studyManagerService.createStudyManager(dto);
        return new ResponseEntity<>(createdManager, HttpStatus.CREATED);
    }

    /**
     * Récupère la liste de tous les StudyManagers.
     * @return Une liste de DTOs.
     */
    @GetMapping
    public ResponseEntity<List<StudyManagerDto>> getAllStudyManagers() {
        return ResponseEntity.ok(studyManagerService.getAllStudyManagers());
    }

    /**
     * Récupère un StudyManager par son ID.
     * @param id L'ID du StudyManager.
     * @return Le DTO du StudyManager.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudyManagerDto> getStudyManagerById(@PathVariable Long id) {
        return ResponseEntity.ok(studyManagerService.getStudyManagerById(id));
    }

    /**
     * Met à jour un StudyManager.
     * @param id L'ID du StudyManager à mettre à jour.
     * @param dto Les nouvelles données.
     * @return Le DTO mis à jour.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudyManagerDto> updateStudyManager(@PathVariable Long id, @Valid @RequestBody StudyManagerDto dto) {
        return ResponseEntity.ok(studyManagerService.updateStudyManager(id, dto));
    }

    /**
     * Supprime un StudyManager.
     * @param id L'ID du StudyManager à supprimer.
     * @return Une réponse sans contenu (204 No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudyManager(@PathVariable Long id) {
        studyManagerService.deleteStudyManager(id);
        return ResponseEntity.noContent().build();
    }
}

