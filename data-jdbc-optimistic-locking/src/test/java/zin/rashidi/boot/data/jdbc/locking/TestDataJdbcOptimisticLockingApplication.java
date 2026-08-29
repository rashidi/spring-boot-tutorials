package zin.rashidi.boot.data.jdbc.locking;

import org.springframework.boot.SpringApplication;

public class TestDataJdbcOptimisticLockingApplication {

    public static void main(String[] args) {
        SpringApplication.from(DataJdbcOptimisticLockingApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
