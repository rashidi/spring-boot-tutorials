package zin.rashidi.boot.batch.jdbcbulkupdate;

import org.springframework.boot.SpringApplication;

public class TestBatchJdbcBulkUpdateApplication {

    public static void main(String[] args) {
        SpringApplication.from(BatchJdbcBulkUpdateApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
