package zin.rashidi.boot.data.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestDataJpaFilteredQueryApplication {

    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>(DockerImageName.parse("mysql:lts"));
    }

    public static void main(String[] args) {
        SpringApplication.from(DataJpaFilteredQueryApplication::main).with(TestDataJpaFilteredQueryApplication.class).run(args);
    }

}
