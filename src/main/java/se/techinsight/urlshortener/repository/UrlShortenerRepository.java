package se.techinsight.urlshortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.techinsight.urlshortener.domain.UrlShortener;

import java.util.Optional;

@Repository
public interface UrlShortenerRepository extends JpaRepository<UrlShortener, Long> {

    Optional<UrlShortener> findByShortenKey(String shortenKey);

    long deleteByShortenKey(String shortenKey);

}
