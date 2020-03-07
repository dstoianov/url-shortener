package se.techinsight.urlshortener;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.techinsight.urlshortener.api.dto.OriginalUrlDto;
import se.techinsight.urlshortener.domain.UrlShortener;
import se.techinsight.urlshortener.service.UrlShortenerService;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Slf4j
@Tag(name = "Url Controller", description = "API for working with URL Controller")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
@RestController
@RequestMapping(value = {"/api/url"})
public class UrlController {

    private final UrlShortenerService service;

    @Operation(summary = "Get URL object by {id}",
            description = "Developers endpoint, for debug reasons only")
    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UrlShortener get(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Get URL object by {shorten_key}")
    @GetMapping(value = "/{shorten_key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UrlShortener getByKey(@PathVariable("shorten_key") @Size(min = 1, max = 6) String shortenKey) {
        return service.findByShortenKey(shortenKey);
    }

    @Operation(summary = "Create URL object")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UrlShortener create(@Valid @RequestBody OriginalUrlDto dto) {
        return service.save(dto);
    }

    @Operation(summary = "Delete URL object by {shorten_key}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(value = "/{shorten_key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("shorten_key") @Size(min = 1, max = 6) String shortenKey) {
        service.delete(shortenKey);
    }

    @Operation(summary = "Update {short_key} by {id}")
    @PutMapping(value = "/{id}/{shorten_key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UrlShortener update(@PathVariable("id") Long id,
                               @PathVariable("shorten_key") @Size(min = 1, max = 6) String shortenKey) {
        return service.update(id, shortenKey);
    }

}