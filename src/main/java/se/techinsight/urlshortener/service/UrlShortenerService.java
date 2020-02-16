package se.techinsight.urlshortener.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.techinsight.urlshortener.api.dto.OriginalUrlDto;
import se.techinsight.urlshortener.domain.UrlShortener;
import se.techinsight.urlshortener.exception.UrlNotFoundException;
import se.techinsight.urlshortener.repository.UrlShortenerRepository;


@Slf4j
@Transactional
@Service
public class UrlShortenerService {

    private final UrlShortenerRepository urlRepository;
    private final IdConverterService idConverterService;

    @Autowired
    public UrlShortenerService(UrlShortenerRepository urlRepository, IdConverterService idConverterService) {
        this.urlRepository = urlRepository;
        this.idConverterService = idConverterService;
    }

    public UrlShortener findUrlById(Long id) {
        log.info("Find url by id '{}'", id);
        return urlRepository.findById(id).orElseThrow(() -> new UrlNotFoundException(id));
    }

    public UrlShortener findById(Long id) {
        log.info("Find by id '{}'", id);
        return urlRepository.findById(id).orElseThrow(() -> new UrlNotFoundException(id));
    }

    public UrlShortener findByShortenKey(String shortenKey) {
        log.info("Find url by key '{}'", shortenKey);
        return urlRepository.findByShortenKey(shortenKey).orElseThrow(() -> new UrlNotFoundException(shortenKey));
    }


    public UrlShortener findWithUpdateUrlById(Long id) {
        UrlShortener entity = findUrlById(id);
        entity.setCount(entity.getCount() + 1);
        log.info("Update counter to '{}'", entity.getCount());
        urlRepository.saveAndFlush(entity);
        return entity;
    }

    public UrlShortener save(OriginalUrlDto dto) {
        log.info("Save new url '{}'..", dto.getLongUrl());
        return urlRepository.findByShortenKey(dto.getShortKey())
                .orElseGet(() -> saveAndShorten(dto));
    }

    private UrlShortener saveAndShorten(OriginalUrlDto originalUrl) {
        UrlShortener shortenUrl = new UrlShortener();
        shortenUrl.setLongUrl(originalUrl.getLongUrl());
        UrlShortener url = urlRepository.save(shortenUrl);

        if (StringUtils.isNotBlank(originalUrl.getShortKey())) {
            url.setShortenKey(originalUrl.getShortKey());
        } else {
            String shortenKey = idConverterService.encode(url.getId());
            url.setShortenKey(shortenKey);
        }
        log.info("Shorten key '{}'", url.getShortenKey());
        return url;
    }

    public void delete(String shortenKey) {
        log.info("Remove url with key '{}'..", shortenKey);
        urlRepository.findByShortenKey(shortenKey)
                .ifPresent(entity -> urlRepository.deleteById(entity.getId()));
        // do not throw an exception to the user even if it is not exist
    }

    public UrlShortener update(Long id, String shortenKey) {
        log.info("Update url with id '{}' with new key '{}'..", id, shortenKey);
        UrlShortener url = findById(id);
        url.setShortenKey(shortenKey);
        return url;
    }
}
