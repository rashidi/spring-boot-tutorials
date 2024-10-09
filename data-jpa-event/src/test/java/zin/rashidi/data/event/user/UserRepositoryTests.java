package zin.rashidi.data.event.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop", includeFilters = @Filter(type = ASSIGNABLE_TYPE, classes = { UserEventPublisher.class, UserValidation.class }))
class UserRepositoryTests {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("Given username rashidi.zin is exist When I create a new user with username rashidi.zin Then error with a message Username is already taken will be thrown")
    void saveWithExistingUsername() {
        em.persistAndFlush(new User("rashidi.zin"));

        assertThatThrownBy(() -> repository.save(new User("rashidi.zin")))
                .hasCauseInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username is already taken");
    }

}