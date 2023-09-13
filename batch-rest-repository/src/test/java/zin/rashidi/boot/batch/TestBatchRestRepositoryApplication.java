package zin.rashidi.boot.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestBatchRestRepositoryApplication {

	@Bean
	@ServiceConnection
	MySQLContainer<?> mysqlContainer() {
		return new MySQLContainer<>(DockerImageName.parse("mysql:latest"));
	}

    @Bean
    @ServiceConnection
    MongoDBContainer mongoDBContainer() { return new MongoDBContainer(DockerImageName.parse("mongo:latest")); }

	public static void main(String[] args) {
		SpringApplication.from(BatchRestRepositoryApplication::main).with(TestBatchRestRepositoryApplication.class).run(args);
	}

}
