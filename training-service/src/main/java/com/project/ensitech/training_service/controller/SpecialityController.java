package com.project.ensitech.training_service.controller;

import com.project.ensitech.training_service.model.dto.specialityDto.SpecialityDto;
import com.project.ensitech.training_service.model.dto.specialityDto.CreateSpecialityDto;
import com.project.ensitech.training_service.service.common.ISpecialityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training/specialities")
@RequiredArgsConstructor
@ControllerAdvice
public class SpecialityController {
    // private static final Logger log = LoggerFactory.getLogger(SpecialityController.class);
    private final ISpecialityService iSpecialityService;



    @GetMapping
    public ResponseEntity<List<SpecialityDto>> getAllSpecialities() {
        return ResponseEntity.ok(iSpecialityService.getAllSpecialities());
    }

    @PostMapping
    public ResponseEntity<SpecialityDto> create(@Valid @RequestBody CreateSpecialityDto createSpecialityDto) {

        SpecialityDto created = iSpecialityService.createSpeciality(createSpecialityDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialityDto> get(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(iSpecialityService.getSpeciality(id));
    }

    @PutMapping
    public ResponseEntity<SpecialityDto> update( @Valid @RequestBody SpecialityDto specialityDto) {
        return ResponseEntity.ok(iSpecialityService.updateSpeciality( specialityDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable Long id) {
        iSpecialityService.deleteSpeciality(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<List<SpecialityDto>> search(@RequestParam  String label) {
        return ResponseEntity.ok(iSpecialityService.searchByLabel(label));
    }
    @GetMapping("by-speciality/{label}")
    public ResponseEntity<SpecialityDto> getSpecialityByLabel(@Valid @PathVariable String label){
        return ResponseEntity.ok(this.iSpecialityService.getSpecialityByLabel(label));
    };
}
