package zin.rashidi.boot.cloud.jdbcenvrepo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import zin.rashidi.boot.cloud.jdbcenvrepo.greet.GreetService;

@Testcontainers
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:tc:mysql:8:///test?TC_INITSCRIPT=init-script.sql"
})
class CloudJdbcEnvRepoApplicationTests {

    @Container
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8");

    @Autowired
    private GreetService service;

	@Test
    @DisplayName("Given app.greet.name is configured to Demo in the database When I call greet Then I should get Hello, my name is Demo")
	void contextLoads() {
        String response = service.greet("Hello");

        assertThat(response).isEqualTo("Hello, my name is Demo");
	}

}
