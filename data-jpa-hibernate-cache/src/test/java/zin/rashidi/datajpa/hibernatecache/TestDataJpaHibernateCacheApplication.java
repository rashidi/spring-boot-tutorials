package zin.rashidi.datajpa.hibernatecache;

import org.springframework.boot.SpringApplication;

public class TestDataJpaHibernateCacheApplication {

	public static void main(String[] args) {
		SpringApplication.from(DataJpaHibernateCacheApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
