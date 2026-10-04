package zin.rashidi.boot.messaging.rabbitmq.dlq.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import zin.rashidi.boot.messaging.rabbitmq.dlq.TestcontainersConfiguration;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Import(TestcontainersConfiguration.class)
@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = RANDOM_PORT)
class PublishUserTests {

    @Autowired
    private RestTestClient client;

    @Autowired
    private UserListener userListener;

    @Autowired
    private UserDlqListener userDlqListener;

    @BeforeEach
    void setUp() {
        userListener.getReceivedUsers().clear();
        userDlqListener.getReceivedDlqUsers().clear();
    }

    @Test
    @DisplayName("When user is published via REST successfully, it should be received by the main queue listener")
    void publish() {
        var user = new User(1L, "Rashidi Zin", "rashidi.zin1", "rashidi@zin.my");

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

    @Test
    @DisplayName("When user fails processing, it should be received by the DLQ listener")
    void publishToDlq() {
        var user = new User(2L, "Rashidi Zin", "rashidi.zin", "rashidi@zin.my");

        client.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .exchange()
                .expectStatus().isAccepted();

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(userDlqListener.getReceivedDlqUsers())
                    .hasSize(1)
                    .extracting("name")
                    .containsExactly("Rashidi Zin")
        );
    }
}
