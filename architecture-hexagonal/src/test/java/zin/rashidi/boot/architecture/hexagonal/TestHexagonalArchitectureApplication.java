package zin.rashidi.boot.architecture.hexagonal;

import org.springframework.boot.SpringApplication;

public class TestHexagonalArchitectureApplication {

    public static void main(String[] args) {
        SpringApplication.from(HexagonalArchitectureApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }

}
