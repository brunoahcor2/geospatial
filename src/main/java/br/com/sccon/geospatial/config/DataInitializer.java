package br.com.sccon.geospatial.config;

import br.com.sccon.geospatial.model.dto.PersonDto;
import br.com.sccon.geospatial.service.PersonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadData(PersonService service) {
        return args -> {
            service.create(new PersonDto(
                    1L,
                    "Bruno Rocha",
                    LocalDate.of(1985, 12, 9),
                    LocalDate.of(2020, 1, 1)
            ));
            service.create(new PersonDto(
                            2L,
                            "Fulano de Tal",
                            LocalDate.of(1978, 3, 25),
                            LocalDate.of(2018, 8, 15)
            ));
            service.create(new PersonDto(
                            3L,
                            "Ciclano de Tal",
                            LocalDate.of(1992, 7, 5),
                            LocalDate.of(2021, 2, 10)
            ));
            service.syncIdGenerator();
        };
    }
}
