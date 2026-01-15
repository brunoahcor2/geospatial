package br.com.sccon.geospatial.model.dto;

import br.com.sccon.geospatial.validator.AtLeastOneField;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@AtLeastOneField
public record PersonPatchDto(
        String name,
        LocalDate birthDate,
        LocalDate admissionDate
) {
}
