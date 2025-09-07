package com.project.academic_service.service;

import com.project.academic_service.dao.repository.AcademicYearRepository;
import com.project.academic_service.dao.repository.PeriodRepository;
import com.project.academic_service.domain.AcademicYear;
import com.project.academic_service.domain.Period;
import com.project.academic_service.dto.PeriodDTO;
import com.project.academic_service.dto.PeriodRestDTO;
import com.project.academic_service.exception.ObjectAlreadyExistsException;
import com.project.academic_service.mapper.PeriodDTOMapper;
import com.project.academic_service.mapper.PeriodEntityMapper;
import com.project.academic_service.mapper.PeriodRestDTOMapper;
import com.project.academic_service.mapper.PeriodUpdateMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PeriodServiceImpl implements IPeriodService{
    private final PeriodRepository periodRepository;
    private final PeriodDTOMapper periodDTOMapper;
    private final PeriodEntityMapper periodEntityMapper;
    private final PeriodUpdateMapper periodUpdateMapper;
    private final AcademicYearRepository academicYearRepository;
    private final PeriodRestDTOMapper periodRestDTOMapper;


    public PeriodServiceImpl(PeriodRepository periodRepository, PeriodDTOMapper periodDTOMapper, PeriodEntityMapper periodEntityMapper, PeriodUpdateMapper periodUpdateMapper, AcademicYearRepository academicYearRepository, PeriodRestDTOMapper periodRestDTOMapper) {
        this.periodRepository = periodRepository;
        this.periodDTOMapper = periodDTOMapper;
        this.periodEntityMapper = periodEntityMapper;
        this.periodUpdateMapper = periodUpdateMapper;
        this.academicYearRepository = academicYearRepository;
        this.periodRestDTOMapper = periodRestDTOMapper;
    }


    @Override
    public PeriodDTO get(int id) {
        return this.periodRepository.findById(id)
                    .map(this.periodDTOMapper)
                .orElseThrow(()-> new NoSuchElementException("There's not field with id"));
    }

    @Override
    public List<PeriodDTO> getAll() {
        return this.periodRepository.findAll()
                .stream()
                .map(this.periodDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<PeriodRestDTO> getAllPeriod() {
        return this.periodRepository.findAll()
                .stream()
                .map(this.periodRestDTOMapper)
                .collect(Collectors.toList());
    }


    @Override
    public Period create(PeriodDTO periodDTO) {
        AcademicYear academicYear = this.academicYearRepository.findById(periodDTO.academicYearId())
                .orElseThrow(()-> new NoSuchElementException("Academic year not found with id: "+ periodDTO.academicYearId()));

        if (this.periodRepository.existsByEntitled(periodDTO.entitled()))
            throw new ObjectAlreadyExistsException("This period already exist!");

        Period entity = this.periodEntityMapper.apply(periodDTO);

        entity.setAcademicYear(academicYear);
        return this.periodRepository.save(entity);

    }

    @Override
    public PeriodDTO update(int id, PeriodDTO periodDTO) {
        Period existingPeriod = this.periodRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("There's no period with that id="+id));

        if(periodDTO.academicYearId() != null){
            if(existingPeriod.getAcademicYear().getId() != periodDTO.academicYearId()){
                AcademicYear academicYear = this.academicYearRepository.findById(periodDTO.academicYearId())
                        .orElseThrow(() -> new NoSuchElementException("Academic year not found with id: "+ periodDTO.academicYearId()) );

                existingPeriod.setAcademicYear(academicYear);
            }
        }
        Period period = this.periodUpdateMapper.apply(periodDTO,existingPeriod);
        Period saved = this.periodRepository.save(period);
        return this.periodDTOMapper.apply(saved);
    }

    @Override
    public void delete(int id) {
        this.periodRepository.deleteById(id);
    }
}
