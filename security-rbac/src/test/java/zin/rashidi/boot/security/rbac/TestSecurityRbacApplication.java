package zin.rashidi.boot.security.rbac;

import org.springframework.boot.SpringApplication;

public class TestSecurityRbacApplication {

    public static void main(String[] args) {
        SpringApplication.from(SecurityRbacApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }

}