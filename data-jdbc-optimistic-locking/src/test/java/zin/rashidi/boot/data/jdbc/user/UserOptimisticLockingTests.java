package zin.rashidi.boot.data.jdbc.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@DataJdbcTest
@Sql(
        executionPhase = BEFORE_TEST_CLASS,
        statements = "CREATE TABLE users (id BIGSERIAL PRIMARY KEY, version BIGINT, name TEXT NOT NULL, username TEXT NOT NULL)"
)
class UserOptimisticLockingTests {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("When a user is persisted Then version field is set to 1")
    void create() {
        var user = repository.save(new User("Rashidi Zin", "rashidi"));

        assertThat(ReflectionTestUtils.getField(user, "version")).isEqualTo(1L);
    }

    @Test
    @DisplayName("Given an existing user When I update its username Then version field is incremented")
    @Sql(statements = "INSERT INTO users (id, version, name, username) VALUES (84, 1, 'Rashidi Zin', 'rashidi');")
    void update() {
        var user = repository.findById(84L).orElseThrow();
        user.username("rashidi.zin");

        var updatedUser = repository.save(user);

        assertThat(ReflectionTestUtils.getField(updatedUser, "version")).isEqualTo(2L);
    }

    @Test
    @DisplayName("Given an outdated version When updating the user Then OptimisticLockingFailureException should be thrown")
    @Sql(statements = "INSERT INTO users (id, version, name, username) VALUES (85, 1, 'Rashidi Zin', 'rashidi');")
    void concurrentUpdate() {
        var firstCopy = repository.findById(85L).orElseThrow();
        var secondCopy = repository.findById(85L).orElseThrow();

        firstCopy.username("rashidi.updated");
        repository.save(firstCopy);

        secondCopy.username("rashidi.concurrent");
        assertThatThrownBy(() -> repository.save(secondCopy))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

}
