package com.project.academic_service.controller;

import com.project.academic_service.domain.Period;
import com.project.academic_service.dto.PeriodDTO;
import com.project.academic_service.dto.PeriodRestDTO;
import com.project.academic_service.service.IPeriodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/period")
public class PeriodController {
    private final IPeriodService periodService;

    public PeriodController(IPeriodService periodService) {
        this.periodService = periodService;
    }

    @GetMapping
    public ResponseEntity<List<PeriodDTO>> getAllPeriod(){
       List<PeriodDTO> periods = this.periodService.getAll();
        return ResponseEntity.ok(periods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeriodDTO> getPeriod(@PathVariable Integer id){
        return ResponseEntity.ok(this.periodService.get(id));
    }

    @GetMapping("/periods")
    public ResponseEntity<List<PeriodRestDTO>> getListOfPeriods(){
        return ResponseEntity.ok(this.periodService.getAllPeriod());
    }

    @PostMapping
    public ResponseEntity<Period> create(@RequestBody PeriodDTO periodDTO){
        Period post = this.periodService.create(periodDTO);
        return ResponseEntity.ok(post);
    }
    @PutMapping("/{id}")
    public ResponseEntity<PeriodDTO> update(@PathVariable Integer id, @RequestBody PeriodDTO periodDTO){
        PeriodDTO put = this.periodService.update(id,periodDTO);
        return ResponseEntity.ok(put);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        this.periodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
