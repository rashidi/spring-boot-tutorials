package zin.rashidi.boot.web.apiversioning;

import org.springframework.boot.SpringApplication;

public class TestApiVersioningApplication {

    public static void main(String[] args) {
        SpringApplication.from(ApiVersioningApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }

}
