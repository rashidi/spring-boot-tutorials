package zin.rashidi.boot.data.envers.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

/**
 * @author Rashidi Zin, GfK
 */
public interface BookRepository extends JpaRepository<Book, Long>, RevisionRepository<Book, Long, Integer> {
}
