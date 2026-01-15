package br.com.sccon.geospatial.controller;

import br.com.sccon.geospatial.exception.ConflictException;
import br.com.sccon.geospatial.exception.NotFoundException;
import br.com.sccon.geospatial.model.dto.PersonDto;
import br.com.sccon.geospatial.model.enums.AgeEnum;
import br.com.sccon.geospatial.model.enums.SalaryEnum;
import br.com.sccon.geospatial.service.PersonService;
import br.com.sccon.geospatial.util.PersonTestProperties;
import br.com.sccon.geospatial.util.Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = PersonController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class
        }
)
@EnableConfigurationProperties(PersonTestProperties.class)
@ActiveProfiles("test")
class PersonControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private PersonService service;

    @Autowired
    private PersonTestProperties testProperties;


    // ==================== FIND ALL ====================

    @Test
    void shouldReturnAllPersons() throws Exception {
        Collection<PersonDto> dtos = testProperties.getPersons()
                .stream()
                .map(p -> new PersonDto(
                        p.getId(),
                        p.getName(),
                        p.getBirthDate(),
                        p.getAdmissionDate()
                ))
                .toList();

        given(service.findAll()).willReturn(dtos);

        mvc.perform(get("/v1/persons")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Bruno Rocha"))
                .andExpect(jsonPath("$[1].name").value("Fulano de Tal"))
                .andExpect(jsonPath("$[2].name").value("Ciclano de Tal"));

        verify(service).findAll();
    }

    // ==================== FIND BY ID ====================

    @Test
    void shouldReturnPersonById() throws Exception {
        PersonDto dto = new PersonDto(
                1L,
                "Bruno Rocha",
                LocalDate.of(1985, 12, 9),
                LocalDate.of(2020, 1, 1)
        );

        when(service.findById(1L)).thenReturn(dto);

        mvc.perform(get("/v1/persons/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Bruno Rocha"));

        verify(service).findById(1L);
    }

    @Test
    void shouldReturn404WhenPersonByIdNotFound() throws Exception {

        when(service.findById(99L))
                .thenThrow(new NotFoundException("Person not found: 99"));

        mvc.perform(get("/v1/persons/{id}", 99))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Person not found: 99"));

        verify(service).findById(99L);
    }


    // ==================== CREATE ====================

    @Test
    void shouldCreatePerson() throws Exception {
        PersonDto dto = new PersonDto(
                null,
                "Bruno Rocha",
                LocalDate.of(1985, 12, 9),
                LocalDate.of(2020, 1, 1)
        );

        PersonDto created = new PersonDto(
                1L,
                "Bruno Rocha",
                LocalDate.of(1985, 12, 9),
                LocalDate.of(2020, 1, 1)
        );

        when(service.create(any(PersonDto.class))).thenReturn(created);

        mvc.perform(post("/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.asJson(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(service).create(any(PersonDto.class));
    }

    @Test
    void shouldReturn409WhenPersonAlreadyExists() throws Exception {
        PersonDto dto = new PersonDto(
                1L,
                "Bruno Rocha",
                LocalDate.of(1985, 12, 9),
                LocalDate.of(2020, 1, 1)
        );

        when(service.create(any(PersonDto.class)))
                .thenThrow(new ConflictException(
                        "Person already exists with id: 1"
                ));

        mvc.perform(post("/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.asJson(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Person already exists with id: 1"));

        verify(service).create(any(PersonDto.class));
    }

    @Test
    void shouldReturn400WithApiErrorOnValidation() throws Exception {

        String json = """
        {
          "name": "Bruno Rocha"
        }
        """;

        mvc.perform(post("/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("birthDate is required; admissionDate is required"))
                .andExpect(jsonPath("$.path").value("/v1/persons"));

        verifyNoInteractions(service);
    }



    // ==================== UPDATE ====================

    @Test
    void shouldUpdatePerson() throws Exception {
        PersonDto dto = new PersonDto(
                1L,
                "Updated Name",
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2020, 1, 1)
        );

        when(service.update(eq(1L), any(PersonDto.class))).thenReturn(dto);

        mvc.perform(put("/v1/persons/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.asJson(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));

        verify(service).update(eq(1L), any(PersonDto.class));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingPerson() throws Exception {
        PersonDto dto = new PersonDto(
                1L,
                "Updated Name",
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2020, 1, 1)
        );

        when(service.update(eq(99L), any(PersonDto.class)))
                .thenThrow(new NotFoundException("Person not found: 99"));

        mvc.perform(put("/v1/persons/{id}", 99)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.asJson(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Person not found: 99"));

        verify(service).update(eq(99L), any(PersonDto.class));
    }


    // ==================== DELETE ====================

    @Test
    void shouldDeletePerson() throws Exception {
        doNothing().when(service).delete(1L);

        mvc.perform(delete("/v1/persons/{id}", 1))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingPerson() throws Exception {
        doThrow(new NotFoundException("Person not found")).when(service).delete(99L);

        mvc.perform(delete("/v1/persons/{id}", 99))
                .andExpect(status().isNotFound());

        verify(service).delete(99L);
    }


    // ==================== AGE ====================

    @Test
    void shouldReturnAgeInYears() throws Exception {
        when(service.calculateAge(1L, AgeEnum.YEARS))
                .thenReturn(Map.of("age", 33));

        mvc.perform(get("/v1/persons/{id}/age", 1)
                        .param("output", "YEARS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(33));

        verify(service).calculateAge(1L, AgeEnum.YEARS);
    }

    @Test
    void shouldReturnBadRequestWhenInvalidAgeEnum() throws Exception {

        mvc.perform(get("/v1/persons/{id}/age", 1)
                        .param("output", "YEAR")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Invalid value 'YEAR' for parameter 'output'. Allowed values are: [DAYS, MONTHS, YEARS]"));

        verifyNoInteractions(service);
    }


    // ==================== SALARY ====================

    @Test
    void shouldReturnFullSalary() throws Exception {
        when(service.calculateSalary(1L, SalaryEnum.FULL))
                .thenReturn(Map.of("salary", 5000));

        mvc.perform(get("/v1/persons/{id}/salary", 1)
                        .param("output", "FULL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salary").value(5000));

        verify(service).calculateSalary(1L, SalaryEnum.FULL);
    }

    @Test
    void shouldReturnBadRequestWhenInvalidSalaryEnum() throws Exception {
        mvc.perform(
                        get("/v1/persons/1/salary")
                                .param("output", "FUL")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Invalid value 'FUL' for parameter 'output'. Allowed values are: [FULL, MIN]"));

        verifyNoInteractions(service);
    }

}
