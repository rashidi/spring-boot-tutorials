package zin.rashidi.data.repositorydefinition.note;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import zin.rashidi.data.repositorydefinition.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;
import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@DataJdbcTest
@SqlMergeMode(MERGE)
@Sql(statements = "CREATE TABLE note (id BIGINT PRIMARY KEY, title VARCHAR(50), content TEXT);", executionPhase = BEFORE_TEST_CLASS)
class NoteRepositoryTests {

    @Autowired
    private NoteRepository notes;

    @Test
    @Sql(statements = {
            "INSERT INTO note (id, title, content) VALUES ('1', 'Right Turn', 'Step forward. Step forward and turn right. Collect.')",
            "INSERT INTO note (id, title, content) VALUES ('2', 'Left Turn', 'Step forward. Reverse and turn left. Collect.')",
            "INSERT INTO note (id, title, content) VALUES ('3', 'Double Spin', 'Syncopated. Double spin. Collect.')"
    })
    @DisplayName("Given there are two entries with the word 'turn' in the title When I search by 'turn' in title Then Right Turn And Left Turn should be returned")
    void findByTitleContainingIgnoreCase() {
        var turns = notes.findByTitleContainingIgnoreCase("turn");

        assertThat(turns)
                .extracting("title")
                .containsOnly("Right Turn", "Left Turn");
    }

}