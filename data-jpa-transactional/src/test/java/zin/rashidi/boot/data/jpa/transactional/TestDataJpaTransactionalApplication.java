package zin.rashidi.boot.data.jpa.transactional;

import org.springframework.boot.SpringApplication;

public class TestDataJpaTransactionalApplication {

    public static void main(String[] args) {
        SpringApplication.from(DataJpaTransactionalApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
