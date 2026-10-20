package zin.rashidi.boot.testcontainers.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
class ClientApplicationTests {

    @Container
    @SuppressWarnings("resource")
    static GenericContainer<?> serverContainer = new GenericContainer<>(DockerImageName.parse("ghcr.io/rashidi/testcontainers-demo-server:latest"))
            .withExposedPorts(8080)
            .withStartupTimeout(java.time.Duration.ofMinutes(2));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        if (serverContainer.isRunning()) {
            registry.add("server.url", () -> "http://" + serverContainer.getHost() + ":" + serverContainer.getMappedPort(8080));
        }
    }

    @Autowired
    private RestClient.Builder restClientBuilder;

    @Test
    @DisplayName("Given the server is running in Testcontainers When the client requests a greeting Then it should return the server's greeting")
    void shouldReturnServerGreeting(@org.springframework.boot.test.web.server.LocalServerPort int port) {
        if (!serverContainer.isRunning()) {
            return;
        }

        RestClient client = restClientBuilder.baseUrl("http://localhost:" + port).build();

        ResponseEntity<Greeting> response = client.get()
                .uri("/client/greetings")
                .retrieve()
                .toEntity(Greeting.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Hello from Testcontainers Server!");
    }
}
