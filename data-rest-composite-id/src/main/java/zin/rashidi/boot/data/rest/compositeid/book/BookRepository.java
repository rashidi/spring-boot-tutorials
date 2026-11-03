package zin.rashidi.boot.data.rest.compositeid.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import zin.rashidi.boot.data.rest.compositeid.book.Book.Isbn;

/**
 * @author Rashidi Zin
 */
@RepositoryRestResource
interface BookRepository extends JpaRepository<Book, Isbn> {
}
