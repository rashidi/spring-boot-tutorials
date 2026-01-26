package zin.rashidi.boot.data.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop", includeFilters = @Filter(EnableJpaAuditing.class))
class UserAuditTests {

    @Container
    @ServiceConnection
    private final static MySQLContainer MYSQL = new MySQLContainer("mysql:lts");

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("When a user is saved Then created and modified fields are set And createdBy and modifiedBy fields are set to Mr. Auditor")
    void create() {
        var user = new User("Rashidi Zin", "rashidi");

        var createdUser = repository.save(user);

        assertThat(createdUser).extracting("created", "modified").isNotNull();
        assertThat(createdUser).extracting("createdBy", "modifiedBy").containsOnly("Mr. Auditor");
    }

    @Test
    @DisplayName("When a user is updated Then modified field should be updated")
    @Sql(statements = "INSERT INTO users (id, name, username, created, modified) VALUES ('84', 'Rashidi Zin', 'rashidi', now() - INTERVAL 7 DAY, now() - INTERVAL 7 DAY)")
    void update() {
        var modifiedUser = repository.findById(84L).map(user -> { user.setUsername("rashidi.zin"); return user; }).map(repository::saveAndFlush).orElseThrow();

        var created = (Instant) ReflectionTestUtils.getField(modifiedUser, "created");
        var modified = (Instant) ReflectionTestUtils.getField(modifiedUser, "modified");

        assertThat(modified).isAfter(created);
    }
}
