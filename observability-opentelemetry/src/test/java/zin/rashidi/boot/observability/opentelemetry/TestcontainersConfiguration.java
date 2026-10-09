package zin.rashidi.boot.observability.opentelemetry;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection(name = "open-telemetry")
    GenericContainer<?> otelContainer() {
        return new GenericContainer<>(DockerImageName.parse("otel/opentelemetry-collector-contrib:latest"))
                .withExposedPorts(4318);
    }
}
