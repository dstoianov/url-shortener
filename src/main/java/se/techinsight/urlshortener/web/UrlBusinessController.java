package se.techinsight.urlshortener.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.techinsight.urlshortener.api.dto.OriginalUrlDto;
import se.techinsight.urlshortener.domain.UrlShortener;
import se.techinsight.urlshortener.repository.UrlShortenerRepository;
import se.techinsight.urlshortener.service.UrlShortenerService;

import javax.validation.Valid;
import java.util.Optional;


@Slf4j
@Tag(name = "Business Logic Controller", description = "Business logic of the service")
@Controller
public class UrlBusinessController {

    @Value("${host.url}")
    private String hostUrl;

    @Autowired
    private UrlShortenerRepository repository;

    @Autowired
    private UrlShortenerService urlShortenerService;

    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("originalUrlDto", new OriginalUrlDto());
        return "index";
    }

    @PostMapping(value = "/")
    public String createLink(@ModelAttribute @Valid OriginalUrlDto originalUrlDto, BindingResult bindingResult,
                             Model model, RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors()
                    .forEach(error -> log.error("Validation {} - {}", error.getObjectName(), error.getDefaultMessage()));
            return "index";
        }

        if (StringUtils.isNotBlank(originalUrlDto.getShortKey()) &&
                !repository.findByShortenKey(originalUrlDto.getShortKey()).isPresent()) {
            String message = String.format("Key '%s' already exists", originalUrlDto.getShortKey());
            log.error(message);
            attributes.addFlashAttribute("message", message);
            return "redirect:/";
        }

        UrlShortener url = urlShortenerService.save(originalUrlDto);
        OriginalUrlDto forPage = new OriginalUrlDto();
        forPage.setLongUrl(url.getLongUrl());
        forPage.setShortKey(String.format("%s/%s", hostUrl, url.getShortenKey()));

        model.addAttribute("data", forPage);
        return "result";
    }

    @GetMapping(value = "/{shorten_str}")
    public ModelAndView redirectToOriginalUrl(@PathVariable("shorten_str") String shortenKey, ModelMap model) {
        Optional<UrlShortener> url = repository.findByShortenKey(shortenKey);
        if (url.isPresent()) {
            log.info("Url exist with id '{}'", shortenKey);
            return new ModelAndView("redirect:" + url.get().getLongUrl());
        }
        log.info("Url does not exist with id '{}'", shortenKey);
        model.addAttribute("url", shortenKey);
        model.addAttribute("message", String.format("Url with id '%s' not found", shortenKey));
        return new ModelAndView("not_found", model);
    }

}
