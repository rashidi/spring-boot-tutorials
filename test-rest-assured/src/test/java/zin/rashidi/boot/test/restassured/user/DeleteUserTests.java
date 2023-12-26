package zin.rashidi.boot.test.restassured.user;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.when;
import static java.time.Duration.ofMinutes;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.testcontainers.utility.MountableFile.forClasspathResource;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
class DeleteUserTests {

    @Container
    @ServiceConnection
    private static final MongoDBContainer mongo = new MongoDBContainer("mongo:latest")
            .withCopyToContainer(forClasspathResource("mongo-init.js"), "/docker-entrypoint-initdb.d/mongo-init.js")
            .withStartupAttempts(2)
            .withStartupTimeout(ofMinutes(10));

    @BeforeAll
    static void port(@LocalServerPort int port) {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Given username zaid.zin exists When I delete with its id Then response status should be No Content")
    void deleteWithValidId() {
        String id = get("/users/{username}", "zaid.zin").path("id");

        when()
                .delete("/users/{id}", id)
        .then().assertThat()
                .statusCode(equalTo(SC_NO_CONTENT));
    }

    @Test
    @DisplayName("When I trigger delete with a non-existing ID Then response status should be Not Found")
    void deleteWithNonExistingId() {
        when()
                .delete("/users/{id}", "5f9b0a9b9d9b4a0a9d9b4a0a")
        .then().assertThat()
                .statusCode(equalTo(SC_NOT_FOUND));
    }

}
