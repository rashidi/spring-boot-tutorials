package zin.rashidi.boot.jdbcscgm.book;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jdbc.core.mapping.schema.LiquibaseChangeSetWriter;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import zin.rashidi.boot.jdbcscgm.TestcontainersConfiguration;

import java.io.IOException;
import java.util.Set;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@DataJdbcTest
class BookRepositoryTests {

    @BeforeAll
    static void generateSchema(@Autowired RelationalMappingContext context) throws IOException {
        context.setInitialEntitySet(Set.of(Author.class, Book.class));

        var writer = new LiquibaseChangeSetWriter(context);
        writer.writeChangeSet(new FileSystemResource("user.yaml"));
    }

    @Autowired
    private BookRepository books;

    @Test
    void findAll() {
        books.findAll();
    }

}
