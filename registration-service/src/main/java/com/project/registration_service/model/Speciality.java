package com.project.registration_service.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Speciality {
    private String label;
    private String description;
    private String cycle;
}
