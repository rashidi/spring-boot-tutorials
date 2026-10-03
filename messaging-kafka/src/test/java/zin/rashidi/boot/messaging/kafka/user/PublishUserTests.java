package zin.rashidi.boot.messaging.kafka.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import zin.rashidi.boot.messaging.kafka.TestcontainersConfiguration;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment =  RANDOM_PORT)
class PublishUserTests {

    @Autowired
    private RestTestClient client;

    @Autowired
    private UserListener userListener;

    @BeforeEach
    void setUp() {
        userListener.getReceivedUsers().clear();
    }

    @Test
    @DisplayName("When user is published via REST, it should be received by the Kafka listener")
    void publish() {
        var user = new User(1L, "Rashidi Zin", "rashidi.zin", "rashidi@zin.my");

        client.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .exchange()
                .expectStatus().isAccepted();

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(userListener.getReceivedUsers())
                    .hasSize(1)
                    .extracting("name")
                    .containsExactly("Rashidi Zin")
        );
    }
}
