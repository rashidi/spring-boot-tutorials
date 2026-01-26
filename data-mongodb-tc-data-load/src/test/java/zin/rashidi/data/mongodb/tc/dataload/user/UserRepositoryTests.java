package zin.rashidi.data.mongodb.tc.dataload.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@DataMongoTest(includeFilters = @Filter(type = ASSIGNABLE_TYPE, classes = UserRepositoryTests.RepositoryPopulatorTestConfiguration.class))
class UserRepositoryTests {

    @Container
    @ServiceConnection
    private static final MongoDBContainer mongo = new MongoDBContainer("mongo:latest");

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

    @TestConfiguration
    static class RepositoryPopulatorTestConfiguration {

        @Bean
        public Jackson2RepositoryPopulatorFactoryBean jacksonRepositoryPopulator() {
            var populator = new Jackson2RepositoryPopulatorFactoryBean();
            populator.setResources(new Resource[] { new ClassPathResource("users.json") });
            return populator;
        }
    }

}
