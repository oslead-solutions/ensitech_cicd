package com.project.user_service.models.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true) // Important pour l'héritage !
// @DiscriminatorValue("1") dit à Hibernate : "Si la colonne 'person_type' contient la valeur 1, alors cette ligne est un Teacher".
@DiscriminatorValue("ADMINISTRATOR")
public class Administrator extends Director {
}
