package zin.rashidi.boot.data.jdbc.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@DataJdbcTest(includeFilters = @Filter(EnableJdbcAuditing.class))
@Sql(
        executionPhase = BEFORE_TEST_CLASS,
        statements = "CREATE TABLE users (id BIGSERIAL PRIMARY KEY, created TIMESTAMP WITH TIME ZONE NOT NULL, created_by TEXT NOT NULL, last_modified TIMESTAMP WITH TIME ZONE NOT NULL, last_modified_by TEXT NOT NULL, name TEXT NOT NULL, username TEXT NOT NULL)"
)
class UserAuditTests {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("When a user is persisted Then created and lastModified fields are set And createdBy and lastModifiedBy fields are set to Mr. Auditor")
    void create() {
        var user = repository.save(new User("Rashidi Zin", "rashidi"));

        assertThat(user).extracting("created", "lastModified").doesNotContainNull();
        assertThat(user).extracting("createdBy", "lastModifiedBy").containsOnly("Mr. Auditor");
    }

    @Test
    @DisplayName("When a user is updated Then lastModified field should be updated")
    @Sql(statements = "INSERT INTO users (id, created, created_by, last_modified, last_modified_by, name, username) VALUES (84, CURRENT_TIMESTAMP - INTERVAL '7 days', 'Mr. Auditor', CURRENT_TIMESTAMP - INTERVAL '7 days', 'Mr. Auditor', 'Rashidi Zin', 'rashidi');")
    void update() {
        var modifiedUser = repository.findById(84L).map(user -> { user.username("rashidi.zin"); return user; }).map(repository::save).orElseThrow();

        var created = (Instant) ReflectionTestUtils.getField(modifiedUser, "created");
        var modified = (Instant) ReflectionTestUtils.getField(modifiedUser, "lastModified");

        assertThat(modified).isAfter(created);
    }

}