package zin.rashidi.boot.data.de.book;

import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

/**
 * @author Rashidi Zin
 */
@RepositoryDefinition(domainClass = Book.class, idClass = Long.class)
public interface BookRepository {

    Optional<Book> findById(Long id);

    Book save(Book entity);

    void delete(Book entity);
}
