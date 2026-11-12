package zin.rashidi.boot.data.rest.book;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Rashidi Zin
 */
@RepositoryRestResource
interface BookRepository extends JpaRepository<Book, UUID> {
}
