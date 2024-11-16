package zin.rashidi.boot.test.restassured.user;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestExecutionListeners;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@TestExecutionListeners(listeners = UserCreationTestExecutionListener.class, mergeMode = MERGE_WITH_DEFAULTS)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class CreateUserTests {

    @Container
    @ServiceConnection
    private static final MongoDBContainer mongo = new MongoDBContainer("mongo:latest");

    @BeforeAll
    static void port(@LocalServerPort int port) {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Given provided username is available When I create a User Then response status should be Created")
    void availableUsername() {
        var content = """
                {
                  "name": "Rashidi Zin",
                  "username": "rashidi.zin"
                }
                """;

        given()
                .contentType(JSON)
                .body(content)
        .when()
                .post("/users")
        .then().assertThat()
                .statusCode(equalTo(SC_CREATED));
    }

    @Test
    @DisplayName("Given the username zaid.zin is unavailable When I create a User Then response status should be Bad Request")
    void unavailableUsername() {
        var content = """
                {
                  "name": "Zaid Zin",
                  "username": "zaid.zin"
                }
                """;

        given()
                .contentType(JSON)
                .body(content)
        .when()
                .post("/users")
        .then().assertThat()
                .statusCode(equalTo(SC_BAD_REQUEST));
    }

}
