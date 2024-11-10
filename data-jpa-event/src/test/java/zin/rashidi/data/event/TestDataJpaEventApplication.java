package zin.rashidi.data.event;

import org.springframework.boot.SpringApplication;

public class TestDataJpaEventApplication {

	public static void main(String[] args) {
		SpringApplication.from(DataJpaEventApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
