package zin.rashidi.boot.jdbcscgm.book;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Rashidi Zin
 */
interface BookRepository extends CrudRepository<Book, Long> {
}
