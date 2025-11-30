package zin.rashidi.boot.jdbi;

import org.springframework.boot.SpringApplication;

public class TestJdbiApplication {

    public static void main(String[] args) {
        SpringApplication.from(JdbiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
