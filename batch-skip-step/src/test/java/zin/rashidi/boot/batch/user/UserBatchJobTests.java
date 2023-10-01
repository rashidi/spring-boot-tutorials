package zin.rashidi.boot.batch.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@SpringBatchTest
@SpringJUnitConfig({ UserBatchJobTests.BatchTestConfiguration.class, UserBatchJobTests.JdbcTestConfiguration.class, UserJobConfiguration.class })
@Sql(
        scripts = {
                "classpath:org/springframework/batch/core/schema-drop-mysql.sql",
                "classpath:org/springframework/batch/core/schema-mysql.sql"
        },
        statements = "CREATE TABLE IF NOT EXISTS users (id BIGINT PRIMARY KEY, name text, username text)"
)
class UserBatchJobTests {

    @Container
    @ServiceConnection
    private final static MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:latest");

    @Autowired
    private JobLauncherTestUtils launcher;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    @DisplayName("Given username Elwyn.Skiles is skipped, When job is executed, Then user is not inserted into database")
    void skipByNullOutput() {

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var execution = launcher.launchJob();
            assertThat(execution.getExitStatus()).isEqualTo(COMPLETED);
        });

        var existsByUsername = jdbc.queryForObject("SELECT EXISTS(SELECT * FROM users WHERE username = 'Elwyn.Skiles')", Boolean.class);

        assertThat(existsByUsername).isFalse();

    }

    @Test
    @DisplayName("Given the username is Maxime_Nienow and UserNotFoundException is thrown, When job is executed, Then user is not inserted into database")
    void skipByException() {

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var execution = launcher.launchJob();
            assertThat(execution.getExitStatus()).isEqualTo(COMPLETED);
        });

        var existsByUsername = jdbc.queryForObject("SELECT EXISTS(SELECT * FROM users WHERE username = 'Maxime_Nienow')", Boolean.class);

        assertThat(existsByUsername).isFalse();

    }

    @AfterEach
    void truncateUsers() {
        jdbc.execute("TRUNCATE TABLE users");
    }

    @TestConfiguration
    static class BatchTestConfiguration extends DefaultBatchConfiguration {

        @Override
        @Bean
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
    static class JdbcTestConfiguration {

            @Bean
            JdbcTemplate jdbcTemplate(DataSource dataSource) {
                return new JdbcTemplate(dataSource);
            }

    }
}