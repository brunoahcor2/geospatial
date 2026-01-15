package br.com.sccon.geospatial.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PersonDto(
        Long id,
        @NotBlank(message = "name is required")
        String name,

        @NotNull(message = "birthDate is required")
        LocalDate birthDate,

        @NotNull(message = "admissionDate is required")
        LocalDate admissionDate
) {
}
