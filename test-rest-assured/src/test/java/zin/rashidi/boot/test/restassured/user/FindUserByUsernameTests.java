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
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@TestExecutionListeners(listeners = UserCreationTestExecutionListener.class, mergeMode = MERGE_WITH_DEFAULTS)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class FindUserByUsernameTests {

    @Container
    @ServiceConnection
    private static final MongoDBContainer mongo = new MongoDBContainer("mongo:latest");

    @BeforeAll
    static void port(@LocalServerPort int port) {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Given username zaid.zin exists When I find a User Then response status should be OK and User should be returned")
    void findByExistingUsername() {
        given()
                .contentType(JSON)
        .when()
                .get("/users/{username}", "zaid.zin")
        .then().assertThat()
                .statusCode(equalTo(SC_OK))
                .body("name", equalTo("Zaid Zin"))
                .body("username", equalTo("zaid.zin"));
    }

    @Test
    @DisplayName("Given there is no User with username rashidi.zin When I find a User Then response status should be Not Found")
    void findByNonExistingUsername() {
        given()
                .contentType(JSON)
        .when()
                .get("/users/{username}", "rashidi.zin")
        .then().assertThat()
                .statusCode(equalTo(SC_NOT_FOUND));
    }

}
