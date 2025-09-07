package com.project.academic_service.config;

import com.project.academic_service.enumeration.AcademicYearEvent;
import com.project.academic_service.enumeration.AcademicYearStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachineFactory
public class AcademicYearStateMachineConfig extends EnumStateMachineConfigurerAdapter<AcademicYearStatus, AcademicYearEvent> {


    @Override
    public void configure(StateMachineStateConfigurer<AcademicYearStatus, AcademicYearEvent> states) throws  Exception{
        states
                .withStates()
                .initial(AcademicYearStatus.EN_PREPARATION)
                .state(AcademicYearStatus.EN_COURS)
                .end(AcademicYearStatus.TERMINEE);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<AcademicYearStatus, AcademicYearEvent> transitions) throws Exception{
        transitions
                .withExternal()
                .source(AcademicYearStatus.EN_PREPARATION)
                .target(AcademicYearStatus.EN_COURS)
                .event(AcademicYearEvent.START)
                .and()
                .withExternal()
                .source(AcademicYearStatus.EN_COURS)
                .target(AcademicYearStatus.TERMINEE)
                .event(AcademicYearEvent.COMPLETE);
    }

}
