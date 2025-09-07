package com.project.academic_service.controller;

import com.project.academic_service.domain.AcademicYear;
import com.project.academic_service.dto.AcademicDTO;
import com.project.academic_service.dto.AcademicYearRestDTO;
import com.project.academic_service.enumeration.AcademicYearEvent;
import com.project.academic_service.mapper.AcademicDTOMapper;
import com.project.academic_service.mapper.AcademicEntityMapper;
import com.project.academic_service.service.AcademicYearServiceImpl;
import com.project.academic_service.service.AcademicYearWorkflowService;
import com.project.academic_service.service.IAcademicYearService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/academic-year")
public class AcademicYearController {

    private final IAcademicYearService academicYearService;
    private final AcademicYearWorkflowService academicYearWorkflowService;
    private final AcademicDTOMapper academicDTOMapper;

    public AcademicYearController(IAcademicYearService academicYearService, AcademicYearWorkflowService academicYearWorkflowService, AcademicEntityMapper academicEntityMapper, AcademicDTOMapper academicDTOMapper) {
        this.academicYearService = academicYearService;
        this.academicYearWorkflowService = academicYearWorkflowService;
        this.academicDTOMapper = academicDTOMapper;

    }

    @GetMapping("/test")
    public String test(){
        return "YES";
    }

    @GetMapping
    public List<AcademicDTO> getAll(){
        return this.academicYearService.getYears();
    }

    @GetMapping("/{id}")
    public AcademicDTO get(@PathVariable("id") Integer id){
        return this.academicYearService.get(id);
    }

    @PostMapping
    public ResponseEntity<AcademicYear> create(@RequestBody AcademicDTO academicDTO){
        AcademicYear post = academicYearService.addAcademicYear(academicDTO);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicDTO> update(@PathVariable Integer id, @RequestBody AcademicDTO academicDTO){
        AcademicDTO put = academicYearService.updateAcademicYear(id, academicDTO);
        return ResponseEntity.ok(put);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        this.academicYearService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AcademicDTO> changeStatus(@PathVariable Integer id, @RequestParam AcademicYearEvent event){
        AcademicYear updatedYearStatus = this.academicYearWorkflowService.changeStatus(id, event);

        return ResponseEntity.ok(this.academicDTOMapper.apply(updatedYearStatus));
    }

    @GetMapping("/by-label/{label}")
    public AcademicYearRestDTO getAcademicYear(@PathVariable("label") String label){
        return this.academicYearService.getAcademicYearByLabel(label);
    }


}
