package zin.rashidi.boot.jdbi.book;

import org.jdbi.v3.spring.JdbiRepository;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Rashidi Zin
 */
@JdbiRepository
@RegisterRowMapper(BookRowMapper.class)
interface BookReadOnlyRepository {

    @SqlQuery("SELECT b.isbn, b.title, a.name AS author FROM book b JOIN author a ON b.author_id = a.id WHERE b.isbn = :isbn")
    Optional<Book> findByIsbn(@Bind String isbn);

    @SqlQuery("SELECT b.isbn, b.title, a.name AS author FROM book b JOIN author a ON b.author_id = a.id WHERE a.name = :author")
    Stream<Book> findByAuthor(@Bind String author);

}
