package se.techinsight.urlshortener.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class OriginalUrlDto {

    @URL
    @NotNull(message = "Please provide valid URL")
    @Size(min = 5, max = 2024)
    @JsonProperty("long_url")
    private String longUrl;

    @Size(min = 2, max = 10)
    @JsonProperty("short_key")
    private String shortKey;
}
