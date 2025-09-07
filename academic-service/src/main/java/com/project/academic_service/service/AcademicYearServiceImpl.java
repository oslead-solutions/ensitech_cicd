package com.project.academic_service.service;

import com.project.academic_service.dao.repository.AcademicYearRepository;
import com.project.academic_service.dao.repository.PeriodRepository;
import com.project.academic_service.domain.AcademicYear;
import com.project.academic_service.domain.Period;
import com.project.academic_service.dto.AcademicDTO;
import com.project.academic_service.dto.AcademicYearRestDTO;
import com.project.academic_service.dto.PeriodDTOCreate;
import com.project.academic_service.exception.NoSuchAcademicYearExistsException;
import com.project.academic_service.exception.ObjectAlreadyExistsException;
import com.project.academic_service.mapper.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AcademicYearServiceImpl implements IAcademicYearService{
    private final AcademicYearRepository academicYearRepository;
    private final AcademicDTOMapper academicDTOMapper;
    private final AcademicEntityMapper academicEntityMapper;
    private final AcademicYearUpdateMapper academicYearUpdateMapper;
    private final PeriodEntityMapper periodEntityMapper;
    private final AcademicYearRestMapper academicYearRestMapper;
    private final PeriodEntityCreateMapper periodEntityCreateMapper;
    private final PeriodRepository periodRepository;

    public AcademicYearServiceImpl(AcademicYearRepository academicYearRepository, AcademicDTOMapper academicDTOMapper, AcademicEntityMapper academicEntityMapper, AcademicYearUpdateMapper academicYearUpdateMapper, PeriodEntityMapper periodEntityMapper, AcademicYearRestMapper academicYearRestMapper, PeriodEntityCreateMapper periodEntityCreateMapper, PeriodRepository periodRepository) {
        this.academicYearRepository = academicYearRepository;
        this.academicDTOMapper = academicDTOMapper;
        this.academicEntityMapper = academicEntityMapper;
        this.academicYearUpdateMapper = academicYearUpdateMapper;
        this.periodEntityMapper = periodEntityMapper;
        this.academicYearRestMapper = academicYearRestMapper;
        this.periodEntityCreateMapper = periodEntityCreateMapper;
        this.periodRepository = periodRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicDTO get(int id) {
        return this.academicYearRepository.findByIdWithPeriods(id)
                .map(academicDTOMapper)
                .orElseThrow(()-> new NoSuchElementException("There nothing about"+ id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicDTO> getYears() {
        return this.academicYearRepository.findAllWithPeriods()
                .stream()
                .map(academicDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public AcademicYear addAcademicYear(AcademicDTO academicDTO) {

        if(this.academicYearRepository.existsByLabel(academicDTO.label()))
            throw new ObjectAlreadyExistsException("Academic year Already exists!");

        if(academicDTO.startDate().isBefore(LocalDate.now()))
            throw new DateTimeException("L'année academique ne peut pas commencer avant la date d'aujourd'hui !");

        for (var periodDTO : academicDTO.periods()) {
            if (periodRepository.existsByStartedAtAndEndedAt(periodDTO.startedAt(), periodDTO.endedAt())) {
                throw new DateTimeException("Une période avec ces dates existe déjà.");
            }
        }

        // Vérifier la validité des périodes par rapport à l'année académique
        if (academicDTO.periods().isEmpty()) {
            throw new IllegalArgumentException("La liste des périodes ne peut pas être vide.");
        }

        boolean areAllPeriodsWithinAcademicYear = academicDTO.periods().stream()
                .allMatch(p -> !p.startedAt().isBefore(academicDTO.startDate()) && !p.endedAt().isAfter(academicDTO.endDate()));

        if (!areAllPeriodsWithinAcademicYear) {
            throw new DateTimeException("Les dates des périodes ne sont pas incluses dans les dates de l'année académique.");
        }

        AcademicYear entity = academicEntityMapper.apply(academicDTO);
        return this.academicYearRepository.save(entity);
    }


    @Override
    public AcademicYear create(AcademicYear academicYear) {
        Optional<AcademicYear> existingYear= this.academicYearRepository.findById(academicYear.getId());
        if(existingYear.isPresent()){
            throw new ObjectAlreadyExistsException("This academic Already exists!");
        }
        academicYear.setCreatedAt(LocalDateTime.now());
        academicYear.setUpdatedAt(LocalDateTime.now());

        return this.academicYearRepository.save(academicYear);
    }

    @Override
    public AcademicYear update(int id, AcademicYear academicYear) {
        AcademicYear existingYear = academicYearRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Academic year not found with id: " + id));

        existingYear.setLabel(academicYear.getLabel());
        existingYear.setStartDate(academicYear.getStartDate());
        existingYear.setEndDate(academicYear.getEndDate());
        existingYear.setStatus(academicYear.getStatus());
        existingYear.setUpdatedAt(LocalDateTime.now());

        return academicYearRepository.save(existingYear);
    }

    @Transactional
    @Override
    public AcademicDTO updateAcademicYear(int id, AcademicDTO academicDTO) {
        AcademicYear existingYear = this.academicYearRepository.findByIdWithPeriods(id)
                .orElseThrow(()-> new NoSuchElementException("Academic year not found with "+ id));

    academicYearUpdateMapper.apply(academicDTO, existingYear);
        if(academicDTO.periods() != null){
            existingYear.getPeriods().clear();

            academicDTO.periods().forEach(periodDTOCreate -> {
                Period period = periodEntityCreateMapper.apply(periodDTOCreate);
                period.setAcademicYear(existingYear); // relation inverse obligatoire
                existingYear.getPeriods().add(period);
            });
        }
        AcademicYear saved = academicYearRepository.save(existingYear);
        return academicDTOMapper.apply(saved);

    }

    @Override
    public void delete(int id) {
        this.academicYearRepository.deleteById(id);
    }

    @Override
    public AcademicYearRestDTO getAcademicYearByLabel(String label) {
        // Une seule requête à la base de données pour trouver l'année académique
        return this.academicYearRepository.findAcademicYearByLabel(label)
                .map(academicYearRestMapper::apply)
                .orElseThrow(() -> new NoSuchAcademicYearExistsException("NO SUCH LABEL FOUND !"));
    }


}
