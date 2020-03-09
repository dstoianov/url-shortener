package se.techinsight.urlshortener;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import se.techinsight.urlshortener.api.dto.OriginalUrlDto;
import se.techinsight.urlshortener.domain.UrlShortener;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@ActiveProfiles({"test"})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/insert_test_data.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:sql/delete_test_data.sql")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class UrlControllerIntegrationTest {

    @Value("http://localhost:${local.server.port}/api/url")
    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void getExistedEntity_OK() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(baseUrl + "/id/1", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    void getNotExistedEntity_NOT_FOUND() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(baseUrl + "/id/9999", String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(response.getBody(), containsString("Could not find url with id : 9999"));
    }

    @Test
    void getNotExistedEntity_BAD_REQUEST() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(baseUrl + "/id/ERROR", String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    void getByExistedKey_OK() {
        ResponseEntity<UrlShortener> response = testRestTemplate.getForEntity(baseUrl + "/g2", UrlShortener.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody().getLongUrl(), equalTo("https://www.google.com.ua"));
    }

    @Test
    void deleteByExistedKey_andGetIt_NOT_FOUND() {
        testRestTemplate.delete(baseUrl + "/g4");
        ResponseEntity<String> response = testRestTemplate.getForEntity(baseUrl + "/g4", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void createEntity_CREATED() {
        var request = buildRequest(new OriginalUrlDto("https://www.test.com", ""));
        ResponseEntity<UrlShortener> response = testRestTemplate.postForEntity(baseUrl + "/", request, UrlShortener.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(response.getBody().getShortenKey(), notNullValue());
    }

    @Test
    void createEntity_andGetByKey() {
        var request = buildRequest(new OriginalUrlDto("https://www.test.com.ua", "mykey"));
        ResponseEntity<UrlShortener> response = testRestTemplate.postForEntity(baseUrl + "/", request, UrlShortener.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(response.getBody().getShortenKey(), equalTo("mykey"));
        assertThat(response.getBody().getLongUrl(), equalTo("https://www.test.com.ua"));

        response = testRestTemplate.getForEntity(baseUrl + "/mykey", UrlShortener.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        assertThat(response.getBody().getLongUrl(), equalTo("https://www.test.com.ua"));
    }

    @Test
    void updateEntityKey_andGetByKey() {
//        update the key
        var response = testRestTemplate.exchange(baseUrl + "/1/deu", HttpMethod.PUT, new HttpEntity<>(getHeaders()), UrlShortener.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody().getShortenKey(), equalTo("deu"));

//        get by newly updated key
        response = testRestTemplate.getForEntity(baseUrl + "/deu", UrlShortener.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    private HttpEntity<OriginalUrlDto> buildRequest(OriginalUrlDto dto) {
        return new HttpEntity<>(dto, getHeaders());
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

}