package se.techinsight.urlshortener.exception;

public class UrlNotFoundException extends RuntimeException {

    public UrlNotFoundException(Long id) {
        super("Could not find url with id : " + id);
    }

    public UrlNotFoundException(String key) {
        super("Could not find url with key : " + key);
    }

}
