package br.com.sccon.geospatial.converter;

import br.com.sccon.geospatial.model.dto.PersonDto;
import br.com.sccon.geospatial.model.entity.Person;

import java.util.function.Function;

public final class PersonMapper {
    private PersonMapper() {}

    public static final Function<Person, PersonDto> TO_DTO =
            person -> new PersonDto(
                    person.getId(),
                    person.getName(),
                    person.getBirthDate(),
                    person.getAdmissionDate()
            );

    public static final Function<PersonDto, Person> TO_ENTITY =
            dto -> new Person(
                    dto.id(),
                    dto.name(),
                    dto.birthDate(),
                    dto.admissionDate()
            );
}
