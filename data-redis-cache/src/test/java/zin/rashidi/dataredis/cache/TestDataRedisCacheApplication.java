package zin.rashidi.dataredis.cache;

import org.springframework.boot.SpringApplication;

public class TestDataRedisCacheApplication {

	public static void main(String[] args) {
		SpringApplication.from(DataRedisCacheApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
