package zin.rashidi.boot.data.mongodb.user;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.context.annotation.FilterType.ANNOTATION;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@DataMongoTest(includeFilters = @Filter(type = ANNOTATION, classes = EnableMongoAuditing.class))
class UserAuditTests {

    @Container
    @ServiceConnection
    private final static MongoDBContainer mongo = new MongoDBContainer("mongo:latest");

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("When a user is saved Then created and modified fields are set And createdBy and modifiedBy fields are set to Mr. Auditor")
    void create() {
        var createdUser = repository.save(new User().name("Rashidi Zin").username("rashidi"));

        assertThat(createdUser).extracting("created", "modified").isNotNull();
        assertThat(createdUser).extracting("createdBy", "modifiedBy").containsOnly("Mr. Auditor");
    }

    @Test
    @DisplayName("When a user is updated Then modified field should be updated")
    void update() {
        var createdUser = repository.save(new User().name("Rashidi Zin").username("rashidi"));

        await().atMost(ofSeconds(1)).untilAsserted(() -> {
            var persistedUser = repository.findById(createdUser.id()).orElseThrow();
            var modifiedUser = repository.save(persistedUser.username("rashidi.zin"));

            assertThat(modifiedUser.modified()).isAfter(createdUser.modified());
        });
    }

}
