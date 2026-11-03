package zin.rashidi.boot.data.jdbc.schemageneration.book;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Rashidi Zin
 */
interface BookRepository extends CrudRepository<Book, Long> {
}
