package zin.rashidi.boot.jdbi.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import zin.rashidi.boot.jdbi.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

/**
 * @author Rashidi Zin
 */
@SpringBootTest(webEnvironment = NONE)
@Import(TestcontainersConfiguration.class)
@Transactional
@Sql(scripts = "classpath:schema.sql", statements = {
        "INSERT INTO author (id, name) VALUES (2, 'Sun Tzu')",
        "INSERT INTO book (isbn, title, author_id) VALUES (9780521531088, 'The Art of War', 2)"
}, executionPhase = BEFORE_TEST_CLASS)
class BookReadOnlyRepositoryTests {

    @Autowired
    private BookReadOnlyRepository books;

    @Test
    @DisplayName("When a book with isbn 9780521531088 is requested Then return the book")
    void findByIsbn() {
        assertThat(books.findByIsbn("9780521531088")).get()
                .extracting("title", "author")
                .containsOnly("The Art of War", "Sun Tzu");
    }

    @Test
    @DisplayName("When a book with author Sun Tzu is requested Then return the book")
    void findByAuthor() {
        assertThat(books.findByAuthor("Sun Tzu"))
                .extracting("isbn", "title")
                .containsOnly(tuple("9780521531088", "The Art of War"));
    }

}