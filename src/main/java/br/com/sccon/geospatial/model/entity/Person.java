package br.com.sccon.geospatial.model.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Person implements Serializable {

    private Long id;
    private String name;
    private LocalDate birthDate;
    private LocalDate admissionDate;

    public void update(Person person) {
        this.name = person.name;
        this.birthDate = person.birthDate;
        this.admissionDate = person.admissionDate;
    }

}
