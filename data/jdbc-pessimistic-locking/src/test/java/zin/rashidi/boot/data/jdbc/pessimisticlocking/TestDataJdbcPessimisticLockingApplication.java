package zin.rashidi.boot.data.jdbc.pessimisticlocking;

import org.springframework.boot.SpringApplication;

/**
 * @author Rashidi Zin
 */
class TestDataJdbcPessimisticLockingApplication {

    public static void main(String[] args) {
        SpringApplication.from(DataJdbcPessimisticLockingApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}