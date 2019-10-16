package myapp.MyAdminPanel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.katharsis.resource.annotations.JsonApiResource;
import lombok.Data;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonApiResource(type = "rate")
@Data
public class CurrencyRate {
    private Double mid;

    private LocalDate effectiveDate;

    private String currency;

    private String code;
}
