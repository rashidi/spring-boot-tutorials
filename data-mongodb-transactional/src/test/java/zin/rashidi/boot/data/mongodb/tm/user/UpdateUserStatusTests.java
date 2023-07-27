package zin.rashidi.boot.data.mongodb.tm.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.context.annotation.FilterType.ANNOTATION;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;
import static zin.rashidi.boot.data.mongodb.tm.user.User.Status.ACTIVE;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import zin.rashidi.boot.data.mongodb.tm.user.UpdateUserStatusTests.UserTestConfiguration;

/**
 * @author Rashidi Zin
 */
@DataMongoTest(includeFilters = {
        @Filter(type = ANNOTATION, classes = EnableTransactionManagement.class),
        @Filter(type = ASSIGNABLE_TYPE, classes = { UpdateUserStatus.class, UserTestConfiguration.class })
})
@Testcontainers
class UpdateUserStatusTests {

    @Container
    @ServiceConnection
    private static final MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse("mongo").withTag("6"));

    @Autowired
    private UserTestService service;

    @Test
    @DisplayName("Given a listener is configured, When save is triggered, Then listener should update the status to ACTIVE before saving")
    void onBeforeSave() {
        var user = service.save(new User("Rashidi Zin", "rashidi.zin"));

        assertThat(user).extracting("status").isEqualTo(ACTIVE);
    }

    @TestConfiguration
    static class UserTestConfiguration {

        @Bean
        public UserTestService userTestService(UserRepository repository) {
            return new UserTestService(repository);
        }

    }

    static class UserTestService {

        private final UserRepository repository;

        UserTestService(UserRepository repository) {
            this.repository = repository;
        }

        @Transactional
        public User save(User user) {
            return repository.save(user);
        }

    }
}
