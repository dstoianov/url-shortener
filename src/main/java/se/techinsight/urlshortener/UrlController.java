package se.techinsight.urlshortener;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.techinsight.urlshortener.api.dto.OriginalUrlDto;
import se.techinsight.urlshortener.domain.UrlShortener;
import se.techinsight.urlshortener.service.UrlShortenerService;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Map;

@Slf4j
@Tag(name = "Url Controller", description = "API for working with URL Controller")
@RestController
@Validated
@RequestMapping({"/api/url"})
public class UrlController {

    @Autowired
    private UrlShortenerService service;

    @Operation(
            tags = "Development",
            summary = "Get URL by ID",
            description = "Developers endpoint, for debug reasons only")
    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UrlShortener get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Get URL by shorten_key")
    @GetMapping(value = "/{shorten_key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UrlShortener getByKey(@PathVariable("shorten_key")
                                 @Size(min = 1, max = 6, message = "must be longer then 1 and less then 6")
                                         String shortenKey) {
        return service.findByShortenKey(shortenKey);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UrlShortener create(@Valid @RequestBody OriginalUrlDto dto) {
        return service.save(dto);
    }

    @DeleteMapping(value = "/{shorten_key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@PathVariable("shorten_key") @Size(min = 1, max = 6) String shortenKey) {
        service.delete(shortenKey);
        return new ResponseEntity<>(Map.of("deleted", Boolean.TRUE), HttpStatus.ACCEPTED);
    }


    @PutMapping(value = "/{url_id}/{shorten_key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UrlShortener update(@PathVariable("url_id") Long id,
                               @PathVariable("shorten_key") @Size(min = 1, max = 6) String shortenKey) {
        return service.update(id, shortenKey);
    }


}