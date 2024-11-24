package zin.rashidi.boot.cloud.jdbcenvrepo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Disabled("Pending compatible Spring Cloud version")
@Testcontainers
@SpringBootTest(properties = "spring.datasource.url=jdbc:tc:mysql:8:///test?TC_INITSCRIPT=init-script.sql", webEnvironment = RANDOM_PORT)
class CloudJdbcEnvRepoApplicationTests {

    @Container
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8");

    @Autowired
    private TestRestTemplate restClient;

	@Test
    @DisplayName("Given app.greet.name is configured to Demo in the database When I call greet Then I should get Hello, my name is Demo")
	void greet() {
        var response = restClient.getForEntity("/greet?greeting={0}", String.class, "Hello");

        assertThat(response.getBody()).isEqualTo("Hello, my name is Demo");
	}

}
