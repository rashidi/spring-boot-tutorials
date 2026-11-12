package zin.rashidi.boot.data.jdbc.locking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataJdbcOptimisticLockingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataJdbcOptimisticLockingApplication.class, args);
    }

}
