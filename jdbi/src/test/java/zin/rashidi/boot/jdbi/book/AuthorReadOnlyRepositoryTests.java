package zin.rashidi.boot.jdbi.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import zin.rashidi.boot.jdbi.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

/**
 * @author Rashidi Zin
 */
@SpringBootTest(webEnvironment = NONE)
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase
@Transactional
@Sql(scripts = "classpath:schema.sql", executionPhase = BEFORE_TEST_CLASS)
class AuthorReadOnlyRepositoryTests {

    @Autowired
    private AuthorReadOnlyRepository authors;

    @Test
    @DisplayName("Given is only one author, When I search for all authors, Then I should get one author")
    void findAll() {
        assertThat(authors.findAll())
                .hasSize(1)
                .extracting("name").containsOnly("Sun Tzu");
    }

    @Test
    @DisplayName("Given there is an author named Sun Tzu, When I search by the name, Then the result should have size of one")
    void findByName() {
        assertThat(authors.findByName("Sun Tzu")).hasSize(1);
    }

}