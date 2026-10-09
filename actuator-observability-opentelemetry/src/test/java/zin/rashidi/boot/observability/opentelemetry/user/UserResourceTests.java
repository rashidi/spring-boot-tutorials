package zin.rashidi.boot.observability.opentelemetry.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@Testcontainers
class UserResourceTests {

    @Container
    private static final GenericContainer<?> otelContainer = new GenericContainer<>("otel/opentelemetry-collector-contrib:latest")
            .withExposedPorts(4318);

    @Autowired
    private RestTestClient client;

    @DynamicPropertySource
    static void otlpProperties(DynamicPropertyRegistry registry) {
        registry.add("management.otlp.tracing.endpoint", () -> "http://" + otelContainer.getHost() + ":" + otelContainer.getMappedPort(4318) + "/v1/traces");
    }

    @Test
    void getUsers() {
        client.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Users");
    }
}
