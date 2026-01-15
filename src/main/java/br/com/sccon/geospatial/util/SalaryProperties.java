package br.com.sccon.geospatial.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "geospatial.salary")
public class SalaryProperties {

    private BigDecimal minimumWage;
    private BigDecimal annualIncreaseRate;
    private BigDecimal annualFixedIncrease;
}
