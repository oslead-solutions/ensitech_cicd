package com.project.registration_service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter @Builder
public class Period {
    private String entitled;
    private String typePeriod;
    private LocalDate startedAt;
    LocalDate endedAt;
}
