package br.com.sccon.geospatial.validator;

import br.com.sccon.geospatial.model.dto.PersonPatchDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneFieldValidator
        implements ConstraintValidator<AtLeastOneField, Object> {

    @Override
    public boolean isValid(
            Object value,
            ConstraintValidatorContext context
    ) {

        if (value == null) {
            return false;
        }

        if (value instanceof PersonPatchDto dto) {
            return dto.name() != null && !dto.name().isBlank()
                    || dto.birthDate() != null
                    || dto.admissionDate() != null;
        }

        return false;
    }
}