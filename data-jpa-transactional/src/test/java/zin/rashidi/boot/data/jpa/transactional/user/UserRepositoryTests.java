package zin.rashidi.boot.data.jpa.transactional.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Sql(statements = "INSERT INTO users (id, name, username) VALUES ('84', 'Rashidi Zin', 'rashidi')", executionPhase = BEFORE_TEST_CLASS)
class UserRepositoryTests {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private UserRepository repository;

    @Autowired
    private JpaTransactionManager tm;

    @Test
    @Transactional(readOnly = true)
    void findByUsername() {
        var user = repository.findByUsername("rashidi");

        assertThat(user).isPresent();
    }

    @Test
    @Transactional
    void updateUsername() {
        repository.findByUsername("rashidi").map(user -> user.username("rashidi.zin")).ifPresent(repository::save);
    }

}