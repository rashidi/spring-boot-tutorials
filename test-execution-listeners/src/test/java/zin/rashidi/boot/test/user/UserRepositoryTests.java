package zin.rashidi.boot.test.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS;
import static zin.rashidi.boot.test.user.User.Status.INACTIVE;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestExecutionListeners;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@DataMongoTest
@TestExecutionListeners(
        listeners = { UserCreationTestExecutionListener.class, UserStatusUpdateTestExecutionListener.class, UserDeletionTestExecutionListener.class },
        mergeMode = MERGE_WITH_DEFAULTS
)
class UserRepositoryTests {

    @Container
    @ServiceConnection
    private static final MongoDBContainer mongo = new MongoDBContainer("mongo:latest");

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("Given there are users with status INACTIVE, When I search for users with status INACTIVE, Then I should get users with status INACTIVE")
    void findByStatus() {
        var inactiveUsers = repository.findByStatus(INACTIVE);

        assertThat(inactiveUsers)
                .hasSize(1)
                .extracting("username")
                .containsOnly("rashidi.zin");
    }

}
