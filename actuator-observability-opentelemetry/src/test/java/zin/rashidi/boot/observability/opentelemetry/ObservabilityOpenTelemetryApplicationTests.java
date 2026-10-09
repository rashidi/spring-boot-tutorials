package zin.rashidi.boot.observability.opentelemetry;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class ObservabilityOpenTelemetryApplicationTests {

    @Container
    private static final GenericContainer<?> otelContainer = new GenericContainer<>("otel/opentelemetry-collector-contrib:latest")
            .withExposedPorts(4318);

    @DynamicPropertySource
    static void otlpProperties(DynamicPropertyRegistry registry) {
        registry.add("management.otlp.tracing.endpoint", () -> "http://" + otelContainer.getHost() + ":" + otelContainer.getMappedPort(4318) + "/v1/traces");
    }

    @Test
    void contextLoads() {
    }
}
