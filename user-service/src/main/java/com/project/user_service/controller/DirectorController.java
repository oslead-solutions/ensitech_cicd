package com.project.user_service.controller;

import com.project.user_service.models.dto.DirectorDto;
import com.project.user_service.service.common.IDirectorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directors") // Endpoint dédié
@RequiredArgsConstructor
public class DirectorController {
    private final IDirectorService directorService;
    /**
     * Crée un nouveau Director. Le mot de passe sera haché par le service.
     * @param dto Les données du Director à créer.
     * @return Le DTO du Director créé (sans le mot de passe).
     */
    @PostMapping
    public ResponseEntity<DirectorDto> createDirector(@Valid @RequestBody DirectorDto dto) {
        DirectorDto createdDirector = directorService.createDirector(dto);
        return new ResponseEntity<>(createdDirector, HttpStatus.CREATED);
    }

    /**
     * Récupère la liste de tous les Directors.
     * @return Une liste de DTOs.
     */
    @GetMapping
    public ResponseEntity<List<DirectorDto>> getAllDirectors() {
        return ResponseEntity.ok(directorService.getAllDirectors());
    }

    /**
     * Récupère un Director par son ID.
     * @param id L'ID du Director.
     * @return Le DTO du Director.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DirectorDto> getDirectorById(@PathVariable Long id) {
        return ResponseEntity.ok(directorService.getDirectorById(id));
    }

    /**
     * Met à jour un Director.
     * @param id L'ID du Director à mettre à jour.
     * @param dto Les nouvelles données.
     * @return Le DTO mis à jour.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DirectorDto> updateDirector(@PathVariable Long id, @Valid @RequestBody DirectorDto dto) {
        return ResponseEntity.ok(directorService.updateDirector(id, dto));
    }

    /**
     * Supprime un Director.
     * @param id L'ID du Director à supprimer.
     * @return Une réponse sans contenu (204 No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDirector(@PathVariable Long id) {
        directorService.deleteDirector(id);
        return ResponseEntity.noContent().build();
    }
}
