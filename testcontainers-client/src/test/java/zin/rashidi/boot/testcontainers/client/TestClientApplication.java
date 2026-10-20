package zin.rashidi.boot.testcontainers.client;

import org.springframework.boot.SpringApplication;

public class TestClientApplication {
	public static void main(String[] args) {
		SpringApplication.from(ClientApplication::main).with(TestcontainersConfiguration.class).run(args);
	}
}
