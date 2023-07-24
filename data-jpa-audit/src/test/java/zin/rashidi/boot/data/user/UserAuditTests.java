package zin.rashidi.boot.data.user;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.context.annotation.FilterType.ANNOTATION;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop", includeFilters = @Filter(type = ANNOTATION, classes = EnableJpaAuditing.class))
class UserAuditTests {

    @Container
    @ServiceConnection
    private final static MySQLContainer MYSQL = new MySQLContainer("mysql:latest");

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("When a user is saved Then created and modified fields are set And createdBy and modifiedBy fields are set to Mr. Auditor")
    void create() {
        var user = new User();

        user.setName("Rashidi Zin");
        user.setUsername("rashidi");

        var createdUser = repository.save(user);

        assertThat(createdUser).extracting("created", "modified").isNotNull();
        assertThat(createdUser).extracting("createdBy", "modifiedBy").containsOnly("Mr. Auditor");
    }

    @Test
    @DisplayName("When a user is updated Then modified field should be updated")
    void update() {
        var user = new User();

        user.setName("Rashidi Zin");
        user.setUsername("rashidi");

        var createdUser = repository.save(user);

        await().atMost(ofSeconds(1)).untilAsserted(() -> {
            createdUser.setUsername("rashidi.zin");

            var modifiedUser = repository.save(createdUser);

            assertThat(modifiedUser.getModified()).isAfter(createdUser.getModified());
        });
    }

}
