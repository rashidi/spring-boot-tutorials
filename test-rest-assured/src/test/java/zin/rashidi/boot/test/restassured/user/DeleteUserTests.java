package zin.rashidi.boot.test.restassured.user;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.when;
import static java.time.Duration.ofMinutes;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.testcontainers.containers.wait.strategy.Wait.forLogMessage;
import static org.testcontainers.utility.MountableFile.forClasspathResource;

import java.time.ZonedDateTime;
import java.util.Date;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.mongodb.core.MongoOperations;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.restassured.RestAssured;

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
            .waitingFor(forLogMessage("(?i).*waiting for connections.*", 2))
            .withStartupTimeout(ofMinutes(1));

    @BeforeAll
    static void port(@LocalServerPort int port) {
        RestAssured.port = port;
    }

    @Test
    void deleteWithValidId() {
        UserTestClient user = get("/users/{username}", "zaid.zin").as(UserTestClient.class);

        when()
                .delete("/users/{id}", user.id())
        .then().assertThat()
                .statusCode(equalTo(SC_NO_CONTENT));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record UserTestClient(String id) {}

}
