package com.project.user_service.controller;

import com.project.user_service.models.dto.AdministratorDto;
import com.project.user_service.models.dto.DirectorDto;
import com.project.user_service.service.common.IAdministratorService;
import com.project.user_service.service.common.IDirectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrators") // Endpoint dédié
@RequiredArgsConstructor
public class AdministratorController {
    private final IAdministratorService administratorService;
    /**
     * Crée un nouveau Administrator. Le mot de passe sera haché par le service.
     * @param dto Les données du Administrator à créer.
     * @return Le DTO du Administrator créé (sans le mot de passe).
     */
    @PostMapping
    public ResponseEntity<AdministratorDto> createAdministrator(@Valid @RequestBody AdministratorDto dto) {
        AdministratorDto createdAdministrator = administratorService.createAdministrator(dto);
        return new ResponseEntity<>(createdAdministrator, HttpStatus.CREATED);
    }

    /**
     * Récupère la liste de tous les Administrators.
     * @return Une liste de DTOs.
     */
    @GetMapping
    public ResponseEntity<List<AdministratorDto>> getAllAdministrators() {
        return ResponseEntity.ok(administratorService.getAllAdministrators());
    }

    /**
     * Récupère un Administrator par son ID.
     * @param id L'ID du Administrator.
     * @return Le DTO du Administrator.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdministratorDto> getAdministratorById(@PathVariable Long id) {
        return ResponseEntity.ok(administratorService.getAdministratorById(id));
    }

    /**
     * Met à jour un Administrator.
     * @param id L'ID du Administrator à mettre à jour.
     * @param dto Les nouvelles données.
     * @return Le DTO mis à jour.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdministratorDto> updateAdministrator(@PathVariable Long id, @Valid @RequestBody AdministratorDto dto) {
        return ResponseEntity.ok(administratorService.updateAdministrator(id, dto));
    }

    /**
     * Supprime un Administrator.
     * @param id L'ID du Administrator à supprimer.
     * @return Une réponse sans contenu (204 No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrator(@PathVariable Long id) {
        administratorService.deleteAdministrator(id);
        return ResponseEntity.noContent().build();
    }
}
