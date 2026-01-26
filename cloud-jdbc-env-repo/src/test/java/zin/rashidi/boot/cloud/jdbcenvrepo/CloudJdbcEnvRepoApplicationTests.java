package zin.rashidi.boot.cloud.jdbcenvrepo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureTestRestTemplate
@Testcontainers
@SpringBootTest(properties = "spring.datasource.url=jdbc:tc:mysql:lts:///test?TC_INITSCRIPT=init-script.sql", webEnvironment = RANDOM_PORT)
class CloudJdbcEnvRepoApplicationTests {

    @Container
    private static final MySQLContainer MYSQL = new MySQLContainer("mysql:lts");

    @Autowired
    private TestRestTemplate restClient;

	@Test
    @DisplayName("Given app.greet.name is configured to Demo in the database When I call greet Then I should get Hello, my name is Demo")
	void greet() {
        var response = restClient.getForEntity("/greet?greeting={0}", String.class, "Hello");

        assertThat(response.getBody()).isEqualTo("Hello, my name is Demo");
	}

}
