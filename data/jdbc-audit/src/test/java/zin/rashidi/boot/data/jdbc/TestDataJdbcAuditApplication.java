package zin.rashidi.boot.data.jdbc;

import org.springframework.boot.SpringApplication;

public class TestDataJdbcAuditApplication {

	public static void main(String[] args) {
		SpringApplication.from(DataJdbcAuditApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
