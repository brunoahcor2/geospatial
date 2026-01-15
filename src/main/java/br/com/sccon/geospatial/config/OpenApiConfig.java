package br.com.sccon.geospatial.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Geospatial API",
                version = "v1",
                description = "API challenge for SCCON company"
        )
)
public class OpenApiConfig {
}
