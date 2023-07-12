package zin.rashidi.boot.data.envers.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.context.annotation.FilterType.ANNOTATION;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author Rashidi Zin, GfK
 */
@Testcontainers
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop", includeFilters = @Filter(type = ANNOTATION, classes = EnableJpaRepositories.class))
class BookRepositoryRevisionTests {

    @Container
    @ServiceConnection
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:latest");

    @Autowired
    private BookRepository repository;

    @Test
    void initialRevision() {
        var book = new Book();

        book.setTitle("The Jungle Book");
        book.setAuthor("Rudyard Kipling");

        var createdBook = repository.save(book);

        var revisions = repository.findRevisions(createdBook.getId());

        assertThat(revisions)
                .isNotEmpty()
                .allSatisfy(revision -> assertThat(revision.getEntity())
                        .extracting("id", "author", "title")
                        .containsExactly(createdBook.getId(), createdBook.getAuthor(), createdBook.getTitle())
                );
    }

}
