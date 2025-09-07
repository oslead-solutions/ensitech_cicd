package com.project.user_service.models.entities;

import com.project.user_service.enumeration.Gender;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
// L'annotation @Inheritance définit la stratégie. SINGLE_TABLE est efficace.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// @DiscriminatorColumn indique le nom de la colonne qui va différencier les types de Personnes.
// Par défaut, le type de la colonne est STRING, mais vous avez mis INTEGER, c'est un choix valide.
@DiscriminatorColumn(name="person_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Person implements Serializable {
    //  @Serial private static final long serialVersionUID = -8885817712041252438L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY est souvent mieux pour MySQL, il laisse la BDD gérer l'auto-incrément.
    private Long id; // Utiliser Long pour les IDs est une bonne pratique, pour éviter les dépassements.

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false) // Un email doit être unique et non nul
    private String email;

    private String address;
    private String telephone;

    @Temporal(TemporalType.DATE) // Précise que seule la date (sans l'heure) nous intéresse.
    private Date birthday;

    @Enumerated(EnumType.STRING) // Stocke l'énumération comme une chaîne ("MALE", "FEMALE") dans la BDD, c'est plus lisible.
    @Column(nullable = false)
    private Gender gender;


    // CONSTRUCTEUR MANUEL NÉCESSAIRE POUR LA CLASSE FILLE (Teacher)
    public Person(Long id, String firstName, String lastName, String email, String address, String telephone, Date birthday, Gender gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.telephone = telephone;
        this.birthday = birthday;
        this.gender = gender;
    }
}
