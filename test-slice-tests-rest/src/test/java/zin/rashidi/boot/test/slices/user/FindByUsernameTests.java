package zin.rashidi.boot.test.slices.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.client.RestTestClient;
import zin.rashidi.boot.test.slices.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;
import static zin.rashidi.boot.test.slices.user.User.Status.ACTIVE;

/**
 * @author Rashidi Zin
 */
@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.security.user.name=rashidi.zin",
        "spring.security.user.password=jU$7d3m0pL3a$eRe|ax"
})
@Sql(executionPhase = BEFORE_TEST_CLASS, statements = "INSERT INTO users (id, first, last, username, status) VALUES (1, 'Rashidi', 'Zin', 'rashidi.zin', 0)")
class FindByUsernameTests {

    @Autowired
    private RestTestClient restClient;

    @Test
    @DisplayName("Given username rashidi.zin exists When I request for the username Then response status should be OK and it should contain the summary of the user")
    void withExistingUsername() {
        var user = restClient.get().uri("/users/{username}", "rashidi.zin")
                .headers(headers -> headers.setBasicAuth("rashidi.zin", "jU$7d3m0pL3a$eRe|ax"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserWithoutId.class)
                .returnResult()
                .getResponseBody();

        assertThat(user)
                .extracting("name", "username", "status")
                .containsExactly("Rashidi Zin", "rashidi.zin", ACTIVE);
    }

    @Test
    @DisplayName("Given username zaid.zin does not exist When I request for the username Then response status should be NOT_FOUND")
    void withNonExistingUsername() {
        restClient.get().uri("/users/{username}", "zaid.zin")
                .headers(headers -> headers.setBasicAuth("rashidi.zin", "jU$7d3m0pL3a$eRe|ax"))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Given there is no authentication When I request for the username Then response status should be UNAUTHORIZED")
    void withoutAuthentication() {
        restClient.get().uri("/users/{username}", "rashidi.zin")
                .exchange()
                .expectStatus().isUnauthorized();
    }

}
