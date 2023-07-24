package zin.rashidi.boot.data.envers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.HamcrestCondition.matching;
import static org.hamcrest.Matchers.greaterThan;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.history.Revision;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import zin.rashidi.boot.data.envers.book.Book;
import zin.rashidi.boot.data.envers.book.BookRepository;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class BookAuditRevisionTests {

    @Container
    @ServiceConnection
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:latest");

    @Autowired
    private BookRepository repository;

    @Test
    @DisplayName("When a book is created, then a revision information is available with revision number 1")
    void create() {
        var book = new Book();

        book.setTitle("The Jungle Book");
        book.setAuthor("Rudyard Kipling");

        var createdBook = repository.save(book);

        var revisions = repository.findRevisions(createdBook.getId());

        assertThat(revisions)
                .hasSize(1)
                .first()
                .extracting(Revision::getRevisionNumber)
                .returns(1, Optional::get);
    }

    @Test
    @DisplayName("When a book is modified, then a revision number will increase")
    void modify() {
        var book = new Book();

        book.setTitle("The Jungle Book");
        book.setAuthor("Rudyard Kipling");

        var createdBook = repository.save(book);

        createdBook.setTitle("If");

        repository.save(createdBook);

        var revisions = repository.findRevisions(createdBook.getId());

        assertThat(revisions)
                .hasSize(2)
                .last()
                .extracting(Revision::getRevisionNumber)
                .extracting(Optional::get).is(matching(greaterThan(1)));
    }

    @Test
    @DisplayName("When a book is removed, then only ID information is available")
    void remove() {
        var book = new Book();

        book.setTitle("The Jungle Book");
        book.setAuthor("Rudyard Kipling");

        var createdBook = repository.save(book);

        repository.delete(createdBook);

        var revision = repository.findLastChangeRevision(createdBook.getId());

        assertThat(revision).get()
                .extracting(Revision::getEntity)
                .extracting("id", "title", "author")
                .containsOnly(createdBook.getId(), null, null);
    }

}
