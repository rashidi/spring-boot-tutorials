package zin.rashidi.boot.data.mongodb.tm.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static zin.rashidi.boot.data.mongodb.tm.user.User.Status.ACTIVE;

/**
 * @author Rashidi Zin
 */
@AutoConfigureTestRestTemplate
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
class CreateUserTests {

    @Container
    @ServiceConnection
    private static final MongoDBContainer mongo = new MongoDBContainer("mongo:latest");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void create() {
        var headers = new HttpHeaders() {{ setContentType(APPLICATION_JSON); }};
        var body = """
                {
                  "username": "rashidi.zin",
                  "name": "Rashidi Zin"
                }
                """;

        var response = restTemplate.exchange("/users", POST, new HttpEntity<>(body, headers), User.class);
        var createdUser = response.getBody();

        assertThat(createdUser).extracting("status").isEqualTo(ACTIVE);
    }

}
