package zin.rashidi.boot.ai.toolcalling;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @author Rashidi Zin
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    GenericContainer<?> wiremockContainer() {
        return new GenericContainer<>(DockerImageName.parse("wiremock/wiremock:3.9.1"))
                .withExposedPorts(8080)
                .withClasspathResourceMapping("mappings", "/home/wiremock/mappings", BindMode.READ_ONLY);
    }

}