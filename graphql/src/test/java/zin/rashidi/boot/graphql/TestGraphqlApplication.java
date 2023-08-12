package zin.rashidi.boot.graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestGraphqlApplication {

	public static void main(String[] args) {
		SpringApplication.from(GraphqlApplication::main).with(TestGraphqlApplication.class).run(args);
	}

}
