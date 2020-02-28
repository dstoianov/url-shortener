package se.techinsight.urlshortener;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UrlControllerIntergationTest {

//    @LocalServerPort
//    private int port; // bind the above RANDOM_PORT

    @Value("http://localhost:${local.server.port}/api/url")
    private String url;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void getExistedEntity_OK() {
        ResponseEntity<String> response = restTemplate.getForEntity(url + "/id/1", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getNotExistedEntity_NOT_FOUND() {
        ResponseEntity<String> response = restTemplate.getForEntity(url + "/id/9999", String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(response.getBody(), containsString("Could not find url with id : 9999"));
    }

    @Test
    public void getNotExistedEntity_BAD_REQUEST() {
        ResponseEntity<String> response = restTemplate.getForEntity(url + "/id/ERROR", String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }


    @Test
    public void getByExistedKey_OK() {
        ResponseEntity<String> response = restTemplate.getForEntity(url + "/g1", String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), containsString("https://www.google.com"));
    }


}