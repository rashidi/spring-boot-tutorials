package zin.rashidi.boot.cloud.jdbcenvrepo;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;


@AutoConfigureRestTestClient
@Testcontainers
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:tc:mysql:lts:///test?TC_INITSCRIPT=init-script.sql",
        "spring.cloud.refresh.additional-property-sources-to-retain=Inlined Test Properties"
}, webEnvironment = RANDOM_PORT)
@DisplayName("Given a JDBC-backed Config Server, When configuration is requested, Then database properties are served")
class CloudJdbcEnvRepoApplicationTests {

    @Container
    private static final MySQLContainer MYSQL = new MySQLContainer("mysql:lts");

    @Autowired
    private RestTestClient restClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

	@Test
    @DisplayName("Given app.greet.name is Demo in the database, When I call greet, Then I get Hello, my name is Demo")
	void greet() {
        restClient.get().uri("/greet?greeting={greeting}", "Hello")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$").isEqualTo("Hello, my name is Demo");
	}

    @Test
    @DisplayName("Given an updated database property, When I refresh, Then greet uses the new value")
    void refreshGreeting() {
        String sql = "UPDATE PROPERTIES SET `VALUE` = ? WHERE `KEY` = ? AND APPLICATION = ? AND PROFILE = ? AND LABEL = ?";
        try {
            assertThat(jdbcTemplate.update(sql, "Updated", "app.greet.name", "demo", "default", "main")).isEqualTo(1);
            restClient.post().uri("/actuator/refresh").exchange().expectStatus().isOk();
            restClient.get().uri("/greet?greeting={greeting}", "Hello")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody().jsonPath("$").isEqualTo("Hello, my name is Updated");
        }
        finally {
            jdbcTemplate.update(sql, "Demo", "app.greet.name", "demo", "default", "main");
            restClient.post().uri("/actuator/refresh").exchange().expectStatus().isOk();
        }
    }

    @Test
    @DisplayName("Given a populated main label, When I request the default configuration, Then the Config Server returns the database property")
    void configServerReturnsConfiguration() {
        restClient.get().uri("/demo/default")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).value(body -> assertThat(body).contains("app.greet.name", "Demo"));
    }

    @Test
    @DisplayName("Given an unknown application, When I request its configuration, Then the Config Server returns not found")
    void configServerRejectsEmptyConfiguration() {
        restClient.get().uri("/unknown/default")
                .exchange()
                .expectStatus().isNotFound();
    }

}
