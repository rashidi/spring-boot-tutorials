package zin.rashidi.boot.observability.opentelemetry.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.client.RestTestClient;
import zin.rashidi.boot.observability.opentelemetry.TestcontainersConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
class UserResourceTests {

    @Autowired
    private RestTestClient client;

    @Test
    void getUsers() {
        client.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Users");
    }
}
