package zin.rashidi.boot.batch.update.user;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

/**
 * @author Rashidi Zin
 */
@SpringBatchTest
@SpringBootTest
@Testcontainers
class UserBatchUpdateJobTests {

    @Container
    @ServiceConnection
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4.2");

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void executeJob(@Autowired Job job) throws Exception {
        jobLauncherTestUtils.setJob(job);

        JobExecution execution = jobLauncherTestUtils.launchJob(
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters()
        );

        assertThat(execution.getExitStatus()).isEqualTo(COMPLETED);

        assertThat(jdbcTemplate.queryForObject("SELECT status FROM users WHERE id = 1", String.class))
                .isEqualTo("ACTIVE");

        assertThat(jdbcTemplate.queryForObject("SELECT status FROM users WHERE id = 2", String.class))
                .isEqualTo("INACTIVE");

        assertThat(jdbcTemplate.queryForObject("SELECT status FROM users WHERE id = 3", String.class))
                .isEqualTo("ACTIVE");
    }

}
