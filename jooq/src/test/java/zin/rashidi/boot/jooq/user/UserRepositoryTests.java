package zin.rashidi.boot.jooq.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@Sql(scripts = "classpath:mysql-schema.sql", statements = "INSERT INTO USERS (name, username) VALUES ('Rashidi Zin', 'rashidi')")
@JooqTest(includeFilters = @Filter(classes = Repository.class))
class UserRepositoryTests {

    @Container
    @ServiceConnection
    private static final MySQLContainer<?> container = new MySQLContainer<>("mysql:latest");

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("Given username rashidi is available, when findByUsername, then return User")
    void findByUsername() {
        var user = repository.findByUsername("rashidi");

        assertThat(user).get()
                .extracting("name", "username")
                .containsOnly("Rashidi Zin", "rashidi");
    }

    @Test
    @DisplayName("Given there is no user with username zaid.zin, when findByUsername, then return empty Optional")
    void findByUsernameWithNonExistingUsername() {
        var user = repository.findByUsername("zaid.zin");

        assertThat(user).isEmpty();
    }

}