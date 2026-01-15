package br.com.sccon.geospatial.service;

import br.com.sccon.geospatial.converter.PersonMapper;
import br.com.sccon.geospatial.exception.ConflictException;
import br.com.sccon.geospatial.exception.NotFoundException;
import br.com.sccon.geospatial.model.dto.PersonDto;
import br.com.sccon.geospatial.model.dto.PersonPatchDto;
import br.com.sccon.geospatial.model.entity.Person;
import br.com.sccon.geospatial.model.enums.AgeEnum;
import br.com.sccon.geospatial.model.enums.SalaryEnum;
import br.com.sccon.geospatial.util.SalaryProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final Map<Long, Person> database = new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(0);

    private final SalaryProperties salaryProperties;

    public void syncIdGenerator() {
        idGenerator.set(
                database.keySet()
                        .stream()
                        .mapToLong(Long::longValue)
                        .max()
                        .orElse(0)
        );
    }

    public Collection<PersonDto> findAll() {
        return database.values()
                .stream()
                .map(PersonMapper.TO_DTO)
                .sorted(Comparator.comparing(PersonDto::name))
                .toList();
    }

    public PersonDto findById(Long id) {
        return Optional.ofNullable(database.get(id))
                .map(PersonMapper.TO_DTO)
                .orElseThrow(() ->
                        new NotFoundException("Person not found: " + id)
                );
    }

    public PersonDto create(PersonDto dto) {

        Long id = Optional.ofNullable(dto.id())
                .orElseGet(idGenerator::incrementAndGet);

        Person person = new Person(
                id,
                dto.name(),
                dto.birthDate(),
                dto.admissionDate()
        );

        Person existing = database.putIfAbsent(id, person);

        if (existing != null) {
            throw new ConflictException(
                    "Person already exists with id: " + id
            );
        }

        return PersonMapper.TO_DTO.apply(person);
    }



    public PersonDto update(Long id, PersonDto dto) {
        return Optional.ofNullable(
                        database.computeIfPresent(id, (k, person) -> {
                            person.update(PersonMapper.TO_ENTITY.apply(dto));
                            return person;
                        })
                )
                .map(PersonMapper.TO_DTO)
                .orElseThrow(() ->
                        new NotFoundException("Person not found: " + id)
                );
    }

    public PersonDto patch(Long id, PersonPatchDto dto) {

        return Optional.ofNullable(
                        database.computeIfPresent(id, (k, person) -> {

                            Optional.ofNullable(dto.name())
                                    .ifPresent(person::setName);

                            Optional.ofNullable(dto.birthDate())
                                    .ifPresent(person::setBirthDate);

                            Optional.ofNullable(dto.admissionDate())
                                    .ifPresent(person::setAdmissionDate);

                            return person;
                        })
                )
                .map(PersonMapper.TO_DTO)
                .orElseThrow(() -> new NotFoundException(
                        "Person not found: " + id
                ));
    }

    public void delete(Long id) {
        Optional.ofNullable(database.remove(id))
                .orElseThrow(() ->
                        new NotFoundException("Person not found: " + id)
                );
    }

    public Map<String, Object> calculateAge(Long id, AgeEnum output) {

        Person person = Optional.ofNullable(database.get(id))
                .orElseThrow(() -> new NotFoundException(
                        "Person not found: " + id
                ));

        LocalDate today = LocalDate.now();
        LocalDate birthDate = person.getBirthDate();

        long age = switch (output) {
            case DAYS -> ChronoUnit.DAYS.between(birthDate, today);
            case MONTHS -> ChronoUnit.MONTHS.between(birthDate, today);
            case YEARS -> ChronoUnit.YEARS.between(birthDate, today);
        };

        return Map.of(
                "id", person.getId(),
                "name", person.getName(),
                "output", output.name().toLowerCase(),
                "age", age
        );
    }

    public Map<String, Object> calculateSalary(Long id, SalaryEnum output) {

        Person person = Optional.ofNullable(database.get(id))
                .orElseThrow(() -> new NotFoundException(
                        "Person not found: " + id
                ));

        BigDecimal minimumWage = salaryProperties.getMinimumWage();
        BigDecimal salary = minimumWage;

        long years = ChronoUnit.YEARS.between(
                person.getAdmissionDate(),
                LocalDate.now()
        );

        for (int i = 0; i < years; i++) {
            salary = salary
                    .add(salary.multiply(
                            salaryProperties.getAnnualIncreaseRate()
                    ))
                    .add(
                            salaryProperties.getAnnualFixedIncrease()
                    );
        }

        salary = salary.setScale(2, RoundingMode.CEILING);

        return switch (output) {
            case FULL -> Map.of(
                    "id", person.getId(),
                    "name", person.getName(),
                    "output", "full",
                    "salary", salary
            );

            case MIN -> {
                BigDecimal minQuantity = salary
                        .divide(minimumWage, 2, RoundingMode.CEILING);

                yield Map.of(
                        "id", person.getId(),
                        "name", person.getName(),
                        "output", "min",
                        "minimumWages", minQuantity
                );
            }
        };
    }

}
