package br.com.sccon.geospatial.controller;

import br.com.sccon.geospatial.model.dto.PersonDto;
import br.com.sccon.geospatial.model.dto.PersonPatchDto;
import br.com.sccon.geospatial.model.enums.AgeEnum;
import br.com.sccon.geospatial.model.enums.SalaryEnum;
import br.com.sccon.geospatial.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/persons")
@Tag(
        name = "Persons",
        description = "Endpoints challenge for SCCON company"
)
public class PersonController {

    private final PersonService service;

    @Operation(
            summary = "List all persons",
            description = "Returns all persons ordered alphabetically by name"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Persons returned successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<PersonDto> findAll() {
        return service.findAll();
    }

    @Operation(
            summary = "Get person by ID",
            description = "Returns a person by its unique identifier"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Person found"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDto findById(
            @Parameter(
                    description = "Person identifier",
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return service.findById(id);
    }

    @Operation(
            summary = "Create a person",
            description = """
                    Creates a new person.
                    If ID is not provided, it will be generated automatically.
                    If ID already exists, a conflict error is returned.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Person created successfully"),
            @ApiResponse(responseCode = "409", description = "Person already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDto create(@Valid @RequestBody PersonDto dto) {
        return service.create(dto);
    }

    @Operation(
            summary = "Update a person",
            description = "Replaces all attributes of an existing person"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Person updated successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonDto update(
            @PathVariable Long id,
            @Valid @RequestBody PersonDto dto
    ) {
        return service.update(id, dto);
    }

    @Operation(
            summary = "Partially update a person",
            description = "Updates one or more attributes of a person"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Person updated successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonDto patch(
            @PathVariable Long id,
            @Valid @RequestBody PersonPatchDto dto
    ) {
        return service.patch(id, dto);
    }

    @Operation(
            summary = "Delete a person",
            description = "Deletes a person by ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Person deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @Operation(
            summary = "Get person's age",
            description = "Returns the person's age in days, months or years"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Age calculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid output parameter"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}/age", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getAge(
            @PathVariable Long id,
            @Parameter(
                    description = "Age output format",
                    example = "YEARS"
            )
            @RequestParam AgeEnum output
    ) {
        return service.calculateAge(id, output);
    }

    @Operation(
            summary = "Get person's salary",
            description = """
                    Returns the person's salary.
                    - FULL: returns the salary value in BRL
                    - MIN: returns how many minimum wages the salary represents
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Salary calculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid output parameter"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}/salary", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getSalary(
            @PathVariable Long id,
            @Parameter(
                    description = "Salary output format",
                    example = "FULL"
            )
            @RequestParam SalaryEnum output
    ) {
        return service.calculateSalary(id, output);
    }
}