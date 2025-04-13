package zin.rashidi.boot.jdbcscgm;

import org.springframework.boot.SpringApplication;

public class TestDataJdbcSchemaGenerationApplication {

    public static void main(String[] args) {
        SpringApplication.from(DataJdbcSchemaGenerationApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
