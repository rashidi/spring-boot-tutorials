package zin.rashidi.boot.messaging.kafka.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * @author Rashidi Zin
 */
@SpringBootTest
@Testcontainers
@AutoConfigureRestTestClient
class UserControllerTests {

    @Container
    @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

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
