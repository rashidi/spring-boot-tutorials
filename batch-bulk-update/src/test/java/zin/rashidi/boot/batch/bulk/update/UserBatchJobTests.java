package zin.rashidi.boot.batch.bulk.update;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.support.JdbcDefaultBatchConfiguration;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.test.JobOperatorTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;
import static org.awaitility.Awaitility.await;
import static zin.rashidi.boot.batch.bulk.update.UserBatchJobTests.BatchTestConfiguration;
import static zin.rashidi.boot.batch.bulk.update.UserBatchJobTests.JdbcTestConfiguration;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@SpringBatchTest
@SpringBootTest(classes = {
        BatchTestConfiguration.class,
        JdbcTestConfiguration.class,
        UserJobConfiguration.class
}, webEnvironment = NONE)
@Sql(
        scripts = {
                "classpath:org/springframework/batch/core/schema-drop-postgresql.sql",
                "classpath:org/springframework/batch/core/schema-postgresql.sql"
        },
        statements = "CREATE TABLE IF NOT EXISTS users (id BIGINT PRIMARY KEY, name text, username text)"
)
class UserBatchJobTests {

    @Container
    @ServiceConnection
    private final static PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private JobOperatorTestUtils operator;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    @DisplayName("Given users exist in database, When job is executed, Then users are updated")
    void updateUsers() {

        IntStream.rangeClosed(1, 10).forEach(i ->
                jdbc.update("INSERT INTO users (id, name, username) VALUES (?, ?, ?)", i, "Old Name " + i, "old_username_" + i)
        );

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var execution = operator.startJob();
            assertThat(execution.getExitStatus()).isEqualTo(COMPLETED);
        });

        var users = jdbc.query("SELECT * FROM users", (rs, _) ->
                new User(rs.getLong("id"), rs.getString("name"), rs.getString("username"))
        );

        assertThat(users)
                .hasSize(10)
                .extracting("username")
                .contains("Bret", "Antonette", "Samantha", "Karianne", "Kamren", "Leopoldo_Corkery", "Elwyn.Skiles", "Maxime_Nienow", "Delphine", "Moriah.Stanton");
    }

    @AfterEach
    void truncateUsers() {
        jdbc.execute("TRUNCATE TABLE users");
    }

    @TestConfiguration
    static class BatchTestConfiguration extends JdbcDefaultBatchConfiguration {

        @Override
        @Bean
        protected DataSource getDataSource() {
            return DataSourceBuilder.create()
                    .url(POSTGRES_CONTAINER.getJdbcUrl())
                    .username(POSTGRES_CONTAINER.getUsername())
                    .password(POSTGRES_CONTAINER.getPassword())
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
