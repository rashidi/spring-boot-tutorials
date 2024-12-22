package zin.rashidi.boot.test.slices.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import zin.rashidi.boot.test.slices.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;
import static zin.rashidi.boot.test.slices.user.User.Status.ACTIVE;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Sql(executionPhase = BEFORE_TEST_CLASS, statements = "INSERT INTO users (id, first, last, username, status) VALUES (1, 'Rashidi', 'Zin', 'rashidi.zin', 0)")
class FindByUsernameTests {

    @Autowired
    private TestRestTemplate restClient;

    @Test
    @DisplayName("Given username rashidi.zin exists When I request for the username Then response status should be OK and it should contain the summary of the user")
    void withExistingUsername() {
        var response = restClient.getForEntity("/users/{username}", UserWithoutId.class, "rashidi.zin");

        assertThat(response.getStatusCode()).isEqualTo(OK);

        var user = response.getBody();

        assertThat(user)
                .extracting("name", "username", "status")
                .containsExactly("Rashidi Zin", "rashidi.zin", ACTIVE);
    }

    @Test
    @DisplayName("Given username zaid.zin does not exist When I request for the username Then response status should be NOT_FOUND")
    void withNonExistingUsername() {
        var response = restClient.getForEntity("/users/{username}", UserWithoutId.class, "zaid.zin");

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

}
