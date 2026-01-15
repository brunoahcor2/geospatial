package br.com.sccon.geospatial.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@ConfigurationProperties(prefix = "test-data")
public class PersonTestProperties {

    private List<PersonYaml> persons;

    @Setter
    @Getter
    public static class PersonYaml {
        private Long id;
        private String name;
        private LocalDate birthDate;
        private LocalDate admissionDate;
    }

}

