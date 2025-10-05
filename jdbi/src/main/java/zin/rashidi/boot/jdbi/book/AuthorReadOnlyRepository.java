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
@RegisterRowMapper(AuthorRowMapper.class)
interface AuthorReadOnlyRepository {

    @SqlQuery("SELECT * FROM author")
    Stream<Author> findAll();

    @SqlQuery("SELECT * FROM author WHERE name = :name")
    Stream<Author> findByName(@Bind("name") String name);

}