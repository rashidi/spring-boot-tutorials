package zin.rashidi.boot.batch.jdbcbulkupdate.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import zin.rashidi.boot.batch.jdbcbulkupdate.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = "spring.batch.jdbc.initialize-schema=always")
@Sql(
        scripts = "classpath:schema-postgres.sql",
        statements = {
                "INSERT INTO users (username, status) VALUES ('rashidi.zin', 'ACTIVE'), ('zaid.zin', 'INACTIVE'), ('john.doe', 'DORMANT')"
        },
        executionPhase = BEFORE_TEST_CLASS)
class BatchJdbcBulkUpdateApplicationTests {

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void contextLoads() {
        var updatedStatuses = jdbc.queryForList("SELECT status FROM users", String.class);

        assertThat(updatedStatuses).containsOnly("INACTIVE", "DORMANT", "DELETED");
    }

}
