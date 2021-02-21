import io.restassured.http.ContentType;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import net.keksipurkki.charles_o_clock.http.HttpVerticle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(VertxExtension.class)
class SmokeIT {

    private final static HttpVerticle sut = new HttpVerticle();

    @BeforeAll
    static void setup(Vertx vertx, VertxTestContext ctx) {
        port = 8080;
        vertx.deployVerticle(sut, ctx.succeedingThenComplete());
    }

    @AfterAll
    static void teardown(Vertx vertx, VertxTestContext ctx) {
        vertx.close(ctx.succeedingThenComplete());
    }

    @Test
    @DisplayName("Random paths map to 404 Not Found")
    public void server_given_some_random_path_then_404(VertxTestContext ctx) {
        given()
            .when().get("/SOME_RANDOM_PATH")
            .then()
            .log().all()
            .assertThat()
            .statusCode(404).and().contentType(ContentType.JSON);
        ctx.completeNow();
    }

    @Test
    @DisplayName("Too long path maps to 414 Request-URI Too Long")
    public void server_given_too_long_uri_then_shortcircuit_to_414(VertxTestContext ctx) {
        var path = String.format("/tags/%s", "A".repeat(4096));
        given()
            .when().get(path)
            .then()
            .assertThat()
            .log().all()
            .statusCode(414);
        ctx.completeNow();
    }

    @Test
    @DisplayName("Too long header value maps to 431 Request Header Fields Too Large")
    public void server_given_too_long_header_then_shortcircuit_to_431(VertxTestContext ctx) {
        given().when()
               .header("Accept", "A".repeat(10 * 1024))
               .get("/")
               .then()
               .assertThat()
               .log()
               .all()
               .statusCode(431);
        ctx.completeNow();
    }

    @Test
    @DisplayName("Request body is so large that request is rejected")
    public void server_given_post_and_too_large_body_then_shortcircuit_to_413(VertxTestContext ctx) {
        var phoneNumber = "A".repeat(8_192);
        var body = new JsonObject().put("phoneNumber", phoneNumber);
        given()
            .log().all()
            .when()
            .header("Content-Type", "application/json")
            .body(body.encode())
            .post("/clients")
            .then()
            .log().all()
            .assertThat()
            .statusCode(413).and().contentType(ContentType.JSON);
        ctx.completeNow();
    }

    @Test
    @DisplayName("Malformed path parameter is a 400 Bad Request")
    public void server_given_malformed_uuid_then_bad_request(VertxTestContext ctx) {
        var malicious = "A".repeat(37);
        given()
            .log().parameters()
            .when().get(String.format("/clients/%s", malicious))
            .then()
            .log().all()
            .assertThat()
            .statusCode(400).and().contentType(ContentType.JSON);
        ctx.completeNow();
    }

    @Test
    @DisplayName("Random resource id is 404 Not Found")
    public void server_given_some_random_uuid_then_not_found(VertxTestContext ctx) {
        var random = UUID.randomUUID();
        given()
            .when().get(String.format("/clients/%s", random))
            .then()
            .log().all()
            .assertThat()
            .statusCode(404).and().contentType(ContentType.JSON);
        ctx.completeNow();
    }

    @ParameterizedTest
    @DisplayName("Syntactically valid but semantically invalid JSON")
    @NullSource
    @ValueSource(strings = {"", " "})
    public void server_given_post_semantically_invalid_json_then_400(String phoneNumber, VertxTestContext ctx) {
        var body = new JsonObject().put("phoneNumber", phoneNumber);
        given()
            .log().all()
            .when()
            .header("Content-Type", "application/json")
            .body(body.encode())
            .post("/clients")
            .then()
            .log().all()
            .assertThat()
            .statusCode(400).and().contentType(ContentType.JSON);
        ctx.completeNow();
    }

    @Test
    @DisplayName("Syntactically invalid JSON")
    public void server_given_post_malformed_json_then_400(VertxTestContext ctx) {
        var body = "{ \"phoneNumber': \"AAAA\"}";
        given()
            .log().all()
            .when()
            .header("Content-Type", "application/json")
            .body(body)
            .post("/clients")
            .then()
            .log().all()
            .assertThat()
            .statusCode(400).and().contentType(ContentType.JSON);
        ctx.completeNow();

    }

    @Test
    public void server_given_malicious_json_then_400() {
        fail();
    }

    @Test
    public void server_given_content_type_mismatch_then_400() {
        fail();
    }

    @Test
    @DisplayName("API requests are not cacheable")
    public void server_given_api_request_then_not_cached(VertxTestContext ctx) {
        given()
            .when()
            .get("/SOME_RANDOM_PATH")
            .then()
            .log()
            .all()
            .assertThat()
            .header("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        ctx.completeNow();
    }

    @Test
    public void server_given_request_with_id_then_copied_to_response() {
        fail();
    }


}