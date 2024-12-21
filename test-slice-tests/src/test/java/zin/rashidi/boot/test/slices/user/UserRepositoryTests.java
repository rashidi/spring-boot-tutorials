package zin.rashidi.boot.test.slices.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static zin.rashidi.boot.test.slices.user.User.Status.ACTIVE;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class UserRepositoryTests {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("Given there the username rashidi.zin exists When I find by the username Then I should receive a summary of the user")
    @Sql(statements = "INSERT INTO users (id, first, last, username, status) VALUES (1, 'Rashidi', 'Zin', 'rashidi.zin', 0)")
    void findByUsername() {
        var user = repository.findByUsername("rashidi.zin");

        assertThat(user).get()
                .extracting("name", "username", "status")
                .containsExactly("Rashidi Zin", "rashidi.zin", ACTIVE);
    }

}