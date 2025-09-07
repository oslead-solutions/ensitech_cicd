package com.project.academic_service.service;

import com.project.academic_service.dao.repository.AcademicYearRepository;
import com.project.academic_service.domain.AcademicYear;
import com.project.academic_service.enumeration.AcademicYearEvent;
import com.project.academic_service.enumeration.AcademicYearStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AcademicYearWorkflowService {
    private final StateMachineFactory<AcademicYearStatus, AcademicYearEvent> factory;
    private final AcademicYearRepository academicYearRepository;

    public AcademicYear changeStatus(int id, AcademicYearEvent event){
        AcademicYear year = academicYearRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Year Not found with id: "+ id));

        StateMachine<AcademicYearStatus, AcademicYearEvent> sm = factory.getStateMachine(String.valueOf(id));

        sm.stop();
        sm.getStateMachineAccessor()
                        .doWithAllRegions(access -> {
                            access.resetStateMachineReactively( new DefaultStateMachineContext<>(year.getStatus(), null, null, null))
                                    .block();
                        }
        );
        sm.start();
        sm.sendEvent(event);

        year.setStatus(sm.getState().getId());
        return academicYearRepository.save(year);

    }
}
