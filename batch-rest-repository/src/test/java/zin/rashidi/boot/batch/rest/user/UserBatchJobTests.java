package zin.rashidi.boot.batch.rest.user;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.mysql.MySQLContainer;

import javax.sql.DataSource;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;
import static zin.rashidi.boot.batch.rest.user.UserBatchJobTests.BatchTestConfiguration;
import static zin.rashidi.boot.batch.rest.user.UserBatchJobTests.MongoTestConfiguration;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@SpringBatchTest
@SpringBootTest(classes = { BatchTestConfiguration.class, MongoTestConfiguration.class, UserJobConfiguration.class }, webEnvironment = NONE)
class UserBatchJobTests {

    @Container
    @ServiceConnection
    private final static MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:lts")
            .withInitScript("org/springframework/batch/core/schema-mysql.sql");

    @Container
    @ServiceConnection
    private final static MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer("mongo:latest");

    @Autowired
    private JobLauncherTestUtils launcher;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    @DisplayName("Given there are 10 users returned from REST Service When the job is COMPLETED Then all users should be saved to MongoDB")
    void launch() {

        await().atMost(ofSeconds(30)).untilAsserted(() -> {
            var execution = launcher.launchJob();

            assertThat(execution.getExitStatus()).isEqualTo(COMPLETED);
        });

        var persistedUsers = mongoOperations.findAll(User.class);

        assertThat(persistedUsers).hasSize(10);
    }

    @TestConfiguration
    static class BatchTestConfiguration extends DefaultBatchConfiguration {

        @Override
        protected DataSource getDataSource() {
            return DataSourceBuilder.create()
                    .url(MYSQL_CONTAINER.getJdbcUrl())
                    .username(MYSQL_CONTAINER.getUsername())
                    .password(MYSQL_CONTAINER.getPassword())
                    .build();
        }

        @Override
        @Bean
        protected PlatformTransactionManager getTransactionManager() {
            return new JdbcTransactionManager(getDataSource());
        }

    }

    @TestConfiguration
    static class MongoTestConfiguration extends AbstractMongoClientConfiguration {

        @Override
        protected String getDatabaseName() {
            return "test";
        }

        @Override
        protected void configureClientSettings(MongoClientSettings.Builder builder) {
            builder.applyConnectionString(new ConnectionString(MONGO_DB_CONTAINER.getReplicaSetUrl()));
        }

    }

}
