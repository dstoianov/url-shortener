package se.techinsight.urlshortener.assured;

import com.github.viclovsky.swagger.coverage.SwaggerCoverageRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import se.techinsight.urlshortener.api.dto.OriginalUrlDto;

import static io.restassured.RestAssured.given;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ActiveProfiles({"test"})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/insert_test_data.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:sql/delete_test_data.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UrlControllerSwaggerIntegrationTest {

    @Value("http://localhost:${local.server.port}")
    private String baseUrl;

    private RequestSpecification spec;

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        spec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .setConfig(RestAssured.config().redirect(redirectConfig().followRedirects(false)))
                .addFilter(new SwaggerCoverageRestAssured())
                .build();
    }

    @Test
    void getExistedEntity_OK() {
        int linkId = 1;
        //@formatter:on
        given()
                .spec(spec)
                .when()
                .get("/api/url/id/{id}", linkId).prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(linkId))
                .body("shorten_key", is("g1"))
        ;
        //@formatter:off
    }

    @Test
    void getNotExistedEntity_NOT_FOUND() {
        int linkId = 9999;
        //@formatter:off
        given()
                .spec(spec)
                .when()
                .get("/api/url/id/{id}", linkId).prettyPeek()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", containsString("Could not find url with id : " + linkId))
        ;
        //@formatter:on
    }

    @Test
    void getNotExistedEntity_BAD_REQUEST() {
        var linkId = "BAD_REQUEST";
        //@formatter:off
        given()
                .spec(spec)
                .when()
                .get("/api/url/id/{id}", linkId).prettyPeek()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
        ;
        //@formatter:on
    }

    @Test
    void deleteByExistedKey_andGetIt_NOT_FOUND() {
        var linkKey = "g4";

        //@formatter:off
        given()
                .spec(spec)
                .when()
                .delete("/api/url/{shorten_key}", linkKey).prettyPeek()
                .then()
                .statusCode(HttpStatus.ACCEPTED.value())
        ;

        given()
                .spec(spec)
                .when()
                .get("/api/url/{shorten_key}", linkKey).prettyPeek()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
        ;
        //@formatter:on
    }

    @Test
    void createEntity_CREATED() {
        var request = new OriginalUrlDto("https://www.test.com", "");
        //@formatter:off
        given()
                .spec(spec)
                .body(request)
                .when()
                .post("/api/url/").prettyPeek()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("long_url", is(request.getLongUrl()))
                .body("shorten_key", notNullValue())
        ;
        //@formatter:on
    }

    @Test
    void createEntity_andGetByKey_OK() {
        var toCreateDto = new OriginalUrlDto("https://www.test.com", "mykey");
        //@formatter:off
        given()
                .spec(spec)
                .body(toCreateDto)
                .when()
                .post("/api/url/").prettyPeek()
                .then()
                .statusCode(HttpStatus.CREATED.value())
        ;

        given()
                .spec(spec)
                .when()
                .get("/api/url/{short_key}", toCreateDto.getShortKey()).prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("long_url", is(toCreateDto.getLongUrl()))
                .body("shorten_key", is(toCreateDto.getShortKey()))
        ;
        //@formatter:on
    }

    @Test
    void getByExistedKey_OK() {
        var shortKey = "g1";
        //@formatter:off
        given()
                .spec(spec)
                .when()
                .get("/api/url/{short_key}", shortKey).prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("long_url", is("https://www.google.com"))
                .body("shorten_key", is(shortKey))
        ;
        //@formatter:on
    }


    @Test
    void updateEntityKey_andGetByKey() {
        var id = 1;
        var shortKey = "deu";
        //@formatter:off
        given()
                .spec(spec)
                .when()
                .put("/api/url/{id}/{short_key}", id, shortKey).prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(id))
                .body("shorten_key", is(shortKey))
        ;
        //@formatter:on
//
////        get by newly updated key
//        response = testRestTemplate.getForEntity(baseUrl + "/deu", UrlShortener.class);
//        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }


    /**
     * WEB Tests
     */


    @Test
    void getByExistedKey_FOUND() {
        var shortenKey = "g2";
        //@formatter:off
        given()
                .spec(spec)
                .when()
                .get("/{shorten_key}", shortenKey).prettyPeek()
                .then()
                .statusCode(HttpStatus.FOUND.value())
                .header("Location", notNullValue())
                .header("Location", equalTo("https://www.google.com.ua"))
        ;
        //@formatter:on
    }


    @Test
    void getIndex_OK() {
        //@formatter:off
        given()
                .spec(spec)
                .when()
                .get("/").prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
        ;
        //@formatter:on
    }


}
