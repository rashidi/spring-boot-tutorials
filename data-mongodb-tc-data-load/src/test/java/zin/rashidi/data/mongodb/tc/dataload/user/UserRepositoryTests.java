package zin.rashidi.data.mongodb.tc.dataload.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static java.time.Duration.ofMinutes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.containers.wait.strategy.Wait.forLogMessage;
import static org.testcontainers.utility.MountableFile.forClasspathResource;

/**
 * @author Rashidi Zin
 */
@DataMongoTest
@Testcontainers
class UserRepositoryTests {

    @Container
    @ServiceConnection
    private static final MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse("mongo").withTag("6"))
            .withCopyToContainer(forClasspathResource("mongo-init.js"), "/docker-entrypoint-initdb.d/mongo-init.js")
            .waitingFor(forLogMessage("(?i).*waiting for connections.*", 1))
            .withStartupAttempts(2)
            .withStartupTimeout(ofMinutes(1));

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("Given there is a user with username rashidi.zin and name Rashidi Zin When I search for username rashidi.zin Then user with provided username should be returned")
    void findByUsername() {
        var user = repository.findByUsername("rashidi.zin");

        assertThat(user)
                .extracting("name")
                .isEqualTo("Rashidi Zin");
    }

    @Test
    @DisplayName("Given there is no user with username zaid.zin When I search for username zaid.zin Then null should be returned")
    void findByUsernameWithNonExistingUsername() {
        var user = repository.findByUsername("zaid.zin");

        assertThat(user).isNull();
    }
}
