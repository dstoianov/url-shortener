package se.techinsight.urlshortener.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OriginalUrlDto {

    @URL(message = "please, provide valid URL")
    @Size(min = 10, max = 2024)
    @JsonProperty("long_url")
    private String longUrl;

    @JsonProperty("short_key")
    private String shortKey;
}
