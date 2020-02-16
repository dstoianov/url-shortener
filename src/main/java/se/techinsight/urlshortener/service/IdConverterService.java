package se.techinsight.urlshortener.service;

import org.springframework.stereotype.Service;

@Service
public class IdConverterService {

    private static final String POSSIBLE_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    //    private static final String POSSIBLE_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ALPHABET_LENGTH = POSSIBLE_ALPHABET.length();


    /**
     * Function to generate a shortenKey from integer ID
     */
    public String encode(long id) {
        StringBuilder shortenKey = new StringBuilder();
        while (id > 0) {
            shortenKey.insert(0, POSSIBLE_ALPHABET.charAt((int) id % ALPHABET_LENGTH));
            id = id / ALPHABET_LENGTH;
        }
        return shortenKey.toString();
    }

    /**
     * Function to get integer ID back from a shortenKey
     */
    public long decode(String base62Encoded) {
        long decode = 0;
        for (int i = 0; i < base62Encoded.length(); i++) {
            decode = decode * ALPHABET_LENGTH + POSSIBLE_ALPHABET.indexOf(base62Encoded.charAt(i));
        }
        return decode;
    }

}
