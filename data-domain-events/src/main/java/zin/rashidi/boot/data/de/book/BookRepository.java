package zin.rashidi.boot.data.de.book;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rashidi Zin
 */
interface BookRepository extends JpaRepository<Book, Long> {
}
