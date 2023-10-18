package zin.rashidi.boot.test.restassured.user;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.time.Duration.ofMinutes;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.testcontainers.containers.wait.strategy.Wait.forLogMessage;
import static org.testcontainers.utility.MountableFile.forClasspathResource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
class FindUserByUsernameTests {

    @Container
    @ServiceConnection
    private static final MongoDBContainer mongo = new MongoDBContainer("mongo:latest")
            .withCopyToContainer(forClasspathResource("mongo-init.js"), "/docker-entrypoint-initdb.d/mongo-init.js")
            .waitingFor(forLogMessage("(?i).*waiting for connections.*", 2))
            .withStartupTimeout(ofMinutes(10));

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
