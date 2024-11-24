package zin.rashidi.boot.data.jpa.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.context.annotation.FilterType.ANNOTATION;
import static zin.rashidi.boot.data.jpa.user.User.Status.ACTIVE;
import static zin.rashidi.boot.data.jpa.user.User.Status.INACTIVE;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@DataJpaTest(
        properties = "spring.jpa.hibernate.ddl-auto=create-drop",
        includeFilters = @Filter(type = ANNOTATION, classes = EnableJpaRepositories.class)
)
class UserRepositoryTests {

    @Container
    @ServiceConnection
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:latest");

    @Autowired
    private UserRepository users;

    @BeforeEach
    void setup() {
        users.saveAll(List.of(
                new User("Rashidi Zin", ACTIVE),
                new User("John Doe", INACTIVE)
        ));
    }

    @Test
    @DisplayName("Given there are two users with status ACTIVE and INACTIVE, when findAll is invoked, then only ACTIVE users are returned")
    void findAll() {
        assertThat(users.findAll())
                .extracting("status")
                .containsOnly(ACTIVE);
    }

}
