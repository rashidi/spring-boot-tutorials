package zin.rashidi.boot.data.mongodb.tm.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static zin.rashidi.boot.data.mongodb.tm.user.User.Status.ACTIVE;

/**
 * @author Rashidi Zin
 */
@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
class CreateUserTests {

    @Container
    @ServiceConnection
    private static final MongoDBContainer mongo = new MongoDBContainer("mongo:latest").withReplicaSet();

    @Autowired
    private RestTestClient restTemplate;

    @Test
    void create() {
        var body = """
                {
                  "username": "rashidi.zin",
                  "name": "Rashidi Zin"
                }
                """;

        var createdUser = restTemplate.post().uri("/users")
                .contentType(APPLICATION_JSON)
                .body(body)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(User.class)
                .returnResult()
                .getResponseBody();

        assertThat(createdUser).extracting("status").isEqualTo(ACTIVE);
    }

}
