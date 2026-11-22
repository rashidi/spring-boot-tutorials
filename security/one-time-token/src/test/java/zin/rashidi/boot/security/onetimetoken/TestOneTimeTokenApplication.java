package zin.rashidi.boot.security.onetimetoken;

import org.springframework.boot.SpringApplication;

public class TestOneTimeTokenApplication {

    public static void main(String[] args) {
        SpringApplication.from(OneTimeTokenApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }

}
